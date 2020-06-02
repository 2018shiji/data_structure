package com.module.data_structure.stream;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * https://zhuanlan.zhihu.com/p/82508008
 * 设计一个stream，对一个树形数据结构进行深度优先遍历
 * @param <T>
 */
public class DFSTreeStream <T> {

    /**
     * 以每个子节点为root递归调用dfsTreeStream，并且flatMap，最终就得到一个
     * 平铺后的先序深优顺序节点序列。
     * 若将Stream.concat里的两个Stream顺序对调，就得到一个后序深优序列。
     */
    /** 使用flatMap + 递归的简单版深优stream实现方案 **/
    public static <T> Stream<T> dfsTreeStream(T root,
                                              Function<T, Stream<T>> childrenGenerator) {
        return Stream.concat(
                Stream.of(root),
                childrenGenerator.apply(root).flatMap(child ->
                    dfsTreeStream(child, childrenGenerator)));
    }

    public List<TreeNode> getTreeNodeBy(T root) {
//        final List<TreeNode> nodes =
//                dfsTreeStream(root, it -> Optional.ofNullable(it.get).orElse(List.of()).stream())
//                .filter()
//                .collect();
        return null;
    }

}
