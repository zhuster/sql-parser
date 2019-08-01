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
package com.wdk.druid.sql.dialect.postgresql.ast.stmt;

import com.wdk.druid.sql.SQLUtils;
import com.wdk.druid.sql.ast.SQLName;
import com.wdk.druid.sql.ast.SQLStatementImpl;
import com.wdk.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.wdk.druid.sql.visitor.SQLASTOutputVisitor;
import com.wdk.druid.sql.visitor.SQLASTVisitor;
import com.wdk.druid.util.JdbcConstants;

public class PGConnectToStatement extends SQLStatementImpl implements PGSQLStatement {
    private SQLName target;

    public PGConnectToStatement() {
        super(JdbcConstants.POSTGRESQL);
    }

    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor) visitor);
    }

    public void output(StringBuffer buf) {
        SQLASTOutputVisitor visitor = SQLUtils.createOutputVisitor(buf, dbType);
        this.accept(visitor);
    }

    @Override
    public void accept0(PGASTVisitor v) {
        if (v.visit(this)) {
            acceptChild(v, target);
        }
        v.endVisit(this);
    }

    public SQLName getTarget() {
        return target;
    }

    public void setTarget(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.target = x;
    }
}
