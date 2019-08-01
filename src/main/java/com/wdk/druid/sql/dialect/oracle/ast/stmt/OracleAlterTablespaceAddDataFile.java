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

import java.util.ArrayList;
import java.util.List;

import com.wdk.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.wdk.druid.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleAlterTablespaceAddDataFile extends OracleSQLObjectImpl implements OracleAlterTablespaceItem {

    private List<OracleFileSpecification> files = new ArrayList<OracleFileSpecification>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, files);
        }
        visitor.endVisit(this);
    }

    public List<OracleFileSpecification> getFiles() {
        return files;
    }

    public void setFiles(List<OracleFileSpecification> files) {
        this.files = files;
    }

}
