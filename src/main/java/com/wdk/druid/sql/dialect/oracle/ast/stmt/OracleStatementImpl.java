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
package com.wdk.druid.sql.dialect.oracle.ast.stmt;

import com.wdk.druid.sql.SQLUtils;
import com.wdk.druid.sql.ast.SQLStatementImpl;
import com.wdk.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.wdk.druid.sql.visitor.SQLASTVisitor;
import com.wdk.druid.util.JdbcConstants;

public abstract class OracleStatementImpl extends SQLStatementImpl implements OracleStatement {
    
    public OracleStatementImpl() {
        super(JdbcConstants.ORACLE);
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public abstract void accept0(OracleASTVisitor visitor);

    public String toString() {
        return SQLUtils.toOracleString(this);
    }
}