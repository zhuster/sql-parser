/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wdk.druid.sql.dialect.mysql.visitor.transform;

import com.wdk.druid.sql.SQLUtils;
import com.wdk.druid.sql.ast.SQLName;
import com.wdk.druid.sql.ast.SQLObject;
import com.wdk.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.wdk.druid.sql.ast.expr.SQLExistsExpr;
import com.wdk.druid.sql.ast.expr.SQLIdentifierExpr;
import com.wdk.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.wdk.druid.sql.ast.expr.SQLPropertyExpr;
import com.wdk.druid.sql.ast.statement.SQLExprTableSource;
import com.wdk.druid.sql.ast.statement.SQLSelect;
import com.wdk.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.wdk.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.wdk.druid.sql.ast.statement.SQLTableSource;
import com.wdk.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.wdk.druid.util.FnvHash;

/**
 * Created by wenshao on 26/07/2017.
 */
public class NameResolveVisitor extends OracleASTVisitorAdapter {
    public boolean visit(SQLIdentifierExpr x) {
        SQLObject parent = x.getParent();

        if (parent instanceof SQLBinaryOpExpr
                && x.getResolvedColumn() == null) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) parent;
            boolean isJoinCondition = binaryOpExpr.getLeft() instanceof SQLName
                    && binaryOpExpr.getRight() instanceof SQLName;
            if (isJoinCondition) {
                return false;
            }
        }

        String name = x.getName();

        if ("ROWNUM".equalsIgnoreCase(name)) {
            return false;
        }

        long hash = x.nameHashCode64();
        SQLTableSource tableSource = null;

        if (hash == FnvHash.Constants.LEVEL
                || hash == FnvHash.Constants.CONNECT_BY_ISCYCLE
                || hash == FnvHash.Constants.SYSTIMESTAMP) {
            return false;
        }

        if (parent instanceof SQLPropertyExpr) {
            return false;
        }

        for (; parent != null; parent = parent.getParent()) {
            if (parent instanceof SQLTableSource) {
                return false;
            }

            if (parent instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) parent;

                if (queryBlock.getInto() != null) {
                    return false;
                }

                if (queryBlock.getParent() instanceof SQLSelect) {
                    SQLObject pp = queryBlock.getParent().getParent();
                    if (pp instanceof SQLInSubQueryExpr || pp instanceof SQLExistsExpr) {
                        return false;
                    }
                }

                SQLTableSource from = queryBlock.getFrom();
                if (from instanceof SQLExprTableSource || from instanceof SQLSubqueryTableSource) {
                    String alias = from.getAlias();
                    if (alias != null) {
                        SQLUtils.replaceInParent(x, new SQLPropertyExpr(alias, name));
                    }
                }
                return false;
            }
        }
        return true;
    }

    public boolean visit(SQLPropertyExpr x) {
        String ownerName = x.getOwnernName();
        if (ownerName == null) {
            return super.visit(x);
        }

        for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) parent;
                SQLTableSource tableSource = queryBlock.findTableSource(ownerName);
                if (tableSource == null) {
                    continue;
                }

                String alias = tableSource.computeAlias();
                if (tableSource != null
                        && ownerName.equalsIgnoreCase(alias)
                        && !ownerName.equals(alias)) {
                    x.setOwner(alias);
                }

                break;
            }
        }

        return super.visit(x);
    }
}
