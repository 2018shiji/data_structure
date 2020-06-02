package com.module.data_structure.stream;

import java.util.List;

/**
 * a tree node data model used for DFS traversal stream generators
 * @param <T>
 */
public class TreeNode<T> {
    private T value;
    private int level;
    private TreeNode<T> parentNode;
    private List<T> path;

    TreeNode(T value, int level, TreeNode<T> parentNode){
        this.value = value;
        this.level = level;
        this.parentNode = parentNode;
    }

    /** 当前节点的数据 **/
    public T getValue() {
        return value;
    }

    /** 当前节点的深度 **/
    public List<T> getPath() {
//        if(this.path == null)
        return null;
    }
}
