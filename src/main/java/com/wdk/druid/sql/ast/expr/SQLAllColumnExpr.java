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
package com.wdk.druid.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.wdk.druid.sql.ast.SQLExprImpl;
import com.wdk.druid.sql.ast.statement.SQLTableSource;
import com.wdk.druid.sql.visitor.SQLASTVisitor;

public final class SQLAllColumnExpr extends SQLExprImpl {
    private transient SQLTableSource resolvedTableSource;

    public SQLAllColumnExpr(){

    }

    public void output(StringBuffer buf) {
        buf.append("*");
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof SQLAllColumnExpr;
    }

    public SQLAllColumnExpr clone() {
        SQLAllColumnExpr x = new SQLAllColumnExpr();

        x.resolvedTableSource = resolvedTableSource;
        return x;
    }

    public SQLTableSource getResolvedTableSource() {
        return resolvedTableSource;
    }

    public void setResolvedTableSource(SQLTableSource resolvedTableSource) {
        this.resolvedTableSource = resolvedTableSource;
    }

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
