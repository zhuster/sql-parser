package com.wdk.druid.sql.struct;

/**
 * @author nero.zz
 * @date 2019/8/1
 */
public class Node {

    private String name;

    private String owner;

    private String alias;

    private boolean table;

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, String owner, String alias) {
        this.name = name;
        this.owner = owner;
        this.alias = alias;
    }

    public Node(String name, String owner, String alias, boolean table) {
        this.name = name;
        this.owner = owner;
        this.alias = alias;
        this.table = table;
    }

    public Node() {
    }

    public String fullName() {
        String fullName = name;
        if (owner != null) {
            fullName = owner + "." + name;
        }
        if (alias != null) {
            fullName = fullName + " AS " + alias;
        }
        return fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isTable() {
        return table;
    }

    public void setTable(boolean table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", alias='" + alias + '\'' +
                ", table=" + table;
    }
}
