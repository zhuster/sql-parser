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
package com.wdk.druid.sql.dialect.odps.visitor;

import com.wdk.druid.sql.ast.SQLExpr;
import com.wdk.druid.sql.ast.SQLName;
import com.wdk.druid.sql.ast.statement.SQLAssignItem;
import com.wdk.druid.sql.ast.statement.SQLCreateTableStatement;
import com.wdk.druid.sql.ast.statement.SQLExprTableSource;
import com.wdk.druid.sql.ast.statement.SQLGrantStatement;
import com.wdk.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.wdk.druid.sql.dialect.hive.ast.HiveInsert;
import com.wdk.druid.sql.dialect.odps.ast.OdpsAddStatisticStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsAnalyzeTableStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsCreateTableStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsGrantStmt;
import com.wdk.druid.sql.dialect.odps.ast.OdpsInsertStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsListStmt;
import com.wdk.druid.sql.dialect.odps.ast.OdpsReadStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsRemoveStatisticStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.wdk.druid.sql.dialect.odps.ast.OdpsSetLabelStatement;
import com.wdk.druid.sql.dialect.odps.ast.OdpsShowGrantsStmt;
import com.wdk.druid.sql.dialect.odps.ast.OdpsShowPartitionsStmt;
import com.wdk.druid.sql.dialect.odps.ast.OdpsShowStatisticStmt;
import com.wdk.druid.sql.dialect.odps.ast.OdpsStatisticClause;
import com.wdk.druid.sql.dialect.odps.ast.OdpsUDTFSQLSelectItem;
import com.wdk.druid.sql.dialect.odps.ast.OdpsValuesTableSource;
import com.wdk.druid.sql.visitor.SchemaStatVisitor;
import com.wdk.druid.stat.TableStat;
import com.wdk.druid.util.JdbcConstants;

public class OdpsSchemaStatVisitor extends SchemaStatVisitor implements OdpsASTVisitor {

    public OdpsSchemaStatVisitor() {
        super(JdbcConstants.ODPS);
    }

    @Override
    public void endVisit(OdpsCreateTableStatement x) {
        super.endVisit((SQLCreateTableStatement) x);
    }

    @Override
    public boolean visit(OdpsCreateTableStatement x) {
        return super.visit((SQLCreateTableStatement) x);
    }

    @Override
    public void endVisit(OdpsInsertStatement x) {

    }

    @Override
    public boolean visit(OdpsInsertStatement x) {
        if (repository != null
                && x.getParent() == null) {
            repository.resolve(x);
        }
        return true;
    }

    @Override
    public void endVisit(HiveInsert x) {

    }

    @Override
    public boolean visit(HiveInsert x) {
        setMode(x, TableStat.Mode.Insert);

        SQLExprTableSource tableSource = x.getTableSource();
        SQLExpr tableName = tableSource.getExpr();

        if (tableName instanceof SQLName) {
            TableStat stat = getTableStat((SQLName) tableName);
            stat.incrementInsertCount();

        }

        for (SQLAssignItem partition : x.getPartitions()) {
            partition.accept(this);
        }

        accept(x.getQuery());

        return false;
    }

    @Override
    public void endVisit(OdpsUDTFSQLSelectItem x) {

    }

    @Override
    public boolean visit(OdpsUDTFSQLSelectItem x) {
        return true;
    }

    @Override
    public void endVisit(OdpsShowPartitionsStmt x) {

    }

    @Override
    public boolean visit(OdpsShowPartitionsStmt x) {
        return true;
    }

    @Override
    public void endVisit(OdpsShowStatisticStmt x) {

    }

    @Override
    public boolean visit(OdpsShowStatisticStmt x) {
        return true;
    }

    @Override
    public void endVisit(OdpsSetLabelStatement x) {

    }

    @Override
    public boolean visit(OdpsSetLabelStatement x) {
        if (x.getTable() != null) {
            x.getTable().accept(this);
        }
        return false;
    }

    @Override
    public void endVisit(OdpsSelectQueryBlock x) {
        super.endVisit((SQLSelectQueryBlock) x);
    }

    @Override
    public boolean visit(OdpsSelectQueryBlock x) {
        return this.visit((SQLSelectQueryBlock) x);
    }
    
    @Override
    public void endVisit(OdpsAnalyzeTableStatement x) {
        
    }
    
    @Override
    public boolean visit(OdpsAnalyzeTableStatement x) {
        return true;
    }

    @Override
    public void endVisit(OdpsAddStatisticStatement x) {
        
    }

    @Override
    public boolean visit(OdpsAddStatisticStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsRemoveStatisticStatement x) {
        
    }
    
    @Override
    public boolean visit(OdpsRemoveStatisticStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsStatisticClause.TableCount x) {
        
    }
    
    @Override
    public boolean visit(OdpsStatisticClause.TableCount x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsStatisticClause.ExpressionCondition x) {
        
    }
    
    @Override
    public boolean visit(OdpsStatisticClause.ExpressionCondition x) {
        return true;
    }

    @Override
    public void endVisit(OdpsStatisticClause.NullValue x) {
        
    }
    
    @Override
    public boolean visit(OdpsStatisticClause.NullValue x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsStatisticClause.ColumnSum x) {
        
    }
    
    @Override
    public boolean visit(OdpsStatisticClause.ColumnSum x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsStatisticClause.ColumnMax x) {
        
    }
    
    @Override
    public boolean visit(OdpsStatisticClause.ColumnMax x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsStatisticClause.ColumnMin x) {
        
    }
    
    @Override
    public boolean visit(OdpsStatisticClause.ColumnMin x) {
        return true;
    }
    
    @Override
    public void endVisit(OdpsReadStatement x) {
        
    }
    
    @Override
    public boolean visit(OdpsReadStatement x) {
        return true;
    }

    @Override
    public void endVisit(OdpsShowGrantsStmt x) {
        
    }

    @Override
    public boolean visit(OdpsShowGrantsStmt x) {
        return true;
    }

    @Override
    public void endVisit(OdpsListStmt x) {
        
    }

    @Override
    public boolean visit(OdpsListStmt x) {
        return true;
    }

    @Override
    public void endVisit(OdpsGrantStmt x) {
        super.endVisit((SQLGrantStatement) x);
    }

    @Override
    public boolean visit(OdpsGrantStmt x) {
        return super.visit((SQLGrantStatement) x);
    }

    @Override
    public void endVisit(OdpsValuesTableSource x) {

    }

    @Override
    public boolean visit(OdpsValuesTableSource x) {
        return false;
    }
}
