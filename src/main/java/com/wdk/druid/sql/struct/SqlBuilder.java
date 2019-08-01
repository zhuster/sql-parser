package com.wdk.druid.sql.struct;

import com.wdk.druid.sql.ast.SQLExpr;
import com.wdk.druid.sql.ast.SQLStatement;
import com.wdk.druid.sql.ast.expr.SQLIdentifierExpr;
import com.wdk.druid.sql.ast.expr.SQLPropertyExpr;
import com.wdk.druid.sql.visitor.SchemaStatVisitor;
import com.wdk.druid.stat.TableStat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nero.zz
 * @date 2019/7/31
 */
public class SqlBuilder {

    private List<Node> sqlNodes = new LinkedList<>();

    private Map<Integer, Node> subNodeMap = new HashMap<>();

    private Map<Integer, Node> tableMap = new HashMap<>();
    /**********用于存储所有没有用表名做限定符的属性名**************/
    private Map<Integer, SQLExpr> sqlExprMap = new HashMap<>();

    private SQLStatement sqlStatement;

    public void build(Node node) {
        sqlNodes.add(node);
        if (node.getOwner() != null) {
            int index = sqlNodes.size() - 1;
            subNodeMap.put(index, node);
        }
        if (node.isTable()) {
            int index = sqlNodes.size() - 1;
            tableMap.put(index, node);
        }
    }

    public void addSqlExpr(SQLExpr sqlExpr) {
        int index = sqlNodes.size() - 1;
        sqlExprMap.put(index, sqlExpr);
    }

    public void accept(SQLStatement statement) {
        sqlStatement = statement;
    }

    /**
     * 删除最近的节点
     */
    public void removeLatestNode() {
        int index = sqlNodes.size() - 1;
        sqlNodes.remove(index);
        subNodeMap.remove(index);
        tableMap.remove(index);
    }

    /**
     * 添加表节点
     *
     * @param node 表节点
     */
    public void addTable(Node node) {
        if (node.isTable()) {
            int index = sqlNodes.size() - 1;
            tableMap.put(index, node);
        }
    }

    /**
     * 得到最近的节点
     *
     * @return 失败则为null，成功则返回正确的节点
     */
    public Node getLatestNode() {
        if (!sqlNodes.isEmpty()) {
            return sqlNodes.get(sqlNodes.size() - 1);
        }
        return null;
    }

    public String generateStandardSql(Map<String, Map<String, String>> replaceColumns) {
        //处理xx.xx的情况
        SchemaStatVisitor visitor = new SchemaStatVisitor();
        sqlStatement.accept(visitor);
        Collection<TableStat.Column> columns = visitor.getColumns();
        //得到代替换表的替换关系 原列名 -> 新列名
        //      Map<String, String> replaceArgs = replaceColumns.get("customer");

        for (TableStat.Column column : columns) {
            sqlExprMap.forEach((k, v) -> {
                if (v instanceof SQLIdentifierExpr) {
                    SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) v;
                    long hashCode64 = identifierExpr.hashCode64();
                    String name = identifierExpr.getName();
                    replaceNodeContent(column, hashCode64, k, name, replaceColumns);
                } else if (v instanceof SQLPropertyExpr) {
                    SQLPropertyExpr propertyExpr = (SQLPropertyExpr) v;
                    long hashCode64 = propertyExpr.hashCode64();
                    String name = propertyExpr.getName();
                    replaceNodeContent(column, hashCode64, k, name, replaceColumns);
                }
            });
        }
        String sql = sqlNodes.stream().map(Node::fullName).collect(Collectors.joining(" "));
        return sql;
    }

    private void replaceNodeContent(TableStat.Column column, Long hashCode64, Integer index,
                                    String name, Map<String, Map<String, String>> replaceColumns) {
        if (column.containsSqlExpr(hashCode64)) {
            Node node = sqlNodes.get(index);
            String ownTable = column.getTable();
            Map<String, String> replaceArgs = replaceColumns.get(ownTable);
            //当前表有需要替换的字段
            if (Objects.nonNull(replaceArgs)) {
                String replaceName = replaceArgs.getOrDefault(name, name);
                node.setName(replaceName);
            }
            sqlNodes.set(index, node);
        }
    }

    @Override
    public String toString() {
        return sqlNodes.stream().map(Node::fullName).collect(Collectors.joining(" "));
    }
}

