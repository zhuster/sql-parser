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
package com.wdk.druid.sql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.wdk.druid.sql.ast.SQLName;
import com.wdk.druid.sql.ast.SQLObject;
import com.wdk.druid.sql.ast.SQLStatementImpl;
import com.wdk.druid.sql.visitor.SQLASTVisitor;

public class SQLAlterDatabaseStatement extends SQLStatementImpl implements SQLAlterStatement {

    private SQLName name;

    private boolean upgradeDataDirectoryName;

    private SQLAlterCharacter character;
    
    public SQLAlterDatabaseStatement() {
        
    }
    
    public SQLAlterDatabaseStatement(String dbType) {
        this.setDbType(dbType);
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }

    public SQLAlterCharacter getCharacter() {
        return character;
    }

    public void setCharacter(SQLAlterCharacter character) {
        if (character != null) {
            character.setParent(this);
        }
        this.character = character;
    }

    public boolean isUpgradeDataDirectoryName() {
        return upgradeDataDirectoryName;
    }

    public void setUpgradeDataDirectoryName(boolean upgradeDataDirectoryName) {
        this.upgradeDataDirectoryName = upgradeDataDirectoryName;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(name);
    }
}