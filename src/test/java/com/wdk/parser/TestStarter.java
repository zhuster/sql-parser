package com.wdk.parser;

import com.wdk.druid.sql.SQLUtils;
import com.wdk.druid.sql.ast.SQLStatement;
import com.wdk.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.wdk.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.wdk.druid.sql.dialect.mysql.visitor.MySqlShowColumnOutpuVisitor;
import com.wdk.druid.sql.struct.SqlBuilder;
import com.wdk.druid.sql.visitor.ExportParameterVisitor;
import com.wdk.druid.sql.visitor.ExportParameterizedOutputVisitor;
import com.wdk.druid.sql.visitor.SQLASTOutputVisitor;
import com.wdk.druid.sql.visitor.SchemaStatVisitor;
import com.wdk.druid.stat.TableStat;
import com.wdk.druid.util.FnvHash;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStarter {


    @Test
    public void test() {
        String sql = "delete from customer where id =1";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        System.out.println(sqlStatement);
    }

    @Test
    public void test1() {
        String sql = "select c.id,c.name from customer c";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        SchemaStatVisitor visitor = new SchemaStatVisitor();
        sqlStatement.accept(visitor);
        Collection<TableStat.Column> columns = visitor.getColumns();
    }


    @Test
    public void test2() {
        long id = FnvHash.hashCode64("c", "name");
        Assert.assertEquals(id, 4190257990408899696L);
    }

    @Test
    public void test3() {
        String sql = "insert into customer (name,age,address) values ('nero',18,'hangzhou')";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        System.out.println(sqlStatement);
        String s = SQLUtils.toMySqlString(sqlStatement);
        System.out.println(s);
    }

    @Test
    public void test4() throws UnsupportedEncodingException {
//        String sql = "select c.name,c.age,c.name,d.address from customer c, department d";
        String sql = "select c.name,c.age,d.star from customer c left join department d on c.name = d.star";
        Map<String,Map<String,String>> replaceParams = new HashMap<>();
        Map<String,String> replaceColumns1 = new HashMap<>();
        replaceColumns1.put("name","new_name");
        replaceColumns1.put("age","new_age");

        Map<String,String> replaceColumns2 = new HashMap<>();
        replaceColumns2.put("star","new_star");


        replaceParams.put("customer",replaceColumns1);
        replaceParams.put("department",replaceColumns2);
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        String standardSql = parser.generateStandardSql(replaceParams);
        System.out.println(standardSql);
    }

    @Test
    public void test5(){
        String name = Hello.hello.name();
        System.out.println(name);
    }
    enum Hello{
        hello
    }
}
