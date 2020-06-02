package com.module.data_structure.stream;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** https://www.zhihu.com/question/291258621/answer/813549365 **/
public class TimerTask {

    /**
     * 给出时间区间和一个指定星期几的集合，返回一个包含符合条件的日期的
     * stream，日期按先后顺序排序。
     * 如果省略结束日期（endDate为null），则返回一个无穷stream
     * 客户代码根据需要自行将其截断（使用takeWhile，limit等方法）
     */
    public static Stream<LocalDate> getDatesInRange(@NonNull LocalDate startDate,
                                                    @NonNull LocalDate endDate, @NonNull Set<DayOfWeek> dayOfWeeks){
        Objects.requireNonNull(startDate, "Must provide a start date");
        //创建第一周的日期列表
        final LocalDate oneWeekLater = startDate.plusWeeks(1);
        final List<LocalDate> datesInAWeek = Stream.iterate(startDate,
                d -> d.isBefore(oneWeekLater), d -> d.plusDays(1))
                .filter(d -> dayOfWeeks == null || dayOfWeeks.contains(d.getDayOfWeek()))
                .collect(Collectors.toList());
        /**
         * 创建一个无穷stream，每次通过前一周的日期列表计算下一周的日期列表
         * 表中每个日期后推一周，然后用flatMap铺平为日期的Stream
         */
        final Stream<LocalDate> dateStream =
                Stream.iterate(datesInAWeek, dates -> dates.stream().map(d -> d.plusWeeks(1)).collect(Collectors.toList())).flatMap(Collection::stream);
        //如果指定了结束日期，则选取到结束日期为止
        return endDate != null ? dateStream.takeWhile(d -> !d.isAfter(endDate)) : dateStream;

    }

    /**
     * 接收一个stream集合和一个Comparator，假定所有传入stream会按照Comparator所定义的顺序供给元素。
     * 返回一个stream来按顺序获取所有stream集合中的元素，排序规则由Comparator定义。
     * Stream中的null值将会被丢弃
     * Stream的接口本身不支持仅取一个元素进行一些操作后接着取后续元素。
     * 如果你用findFirst方法，第二次再次调取这个stream上的任何方法就会抛异常，说stream已经被使用过了。
     * 因此只能把stream转化成iterator再用。
     * 另一个需要考虑的地方是，如果优先队列里直接放元素，在取出元素后没有办法把元素
     * 和它来源的Iterator联系起来。所以在优先队列里实际放的应该是一个同事包含元素和来源Iterator的数据结构：
     * 即apache common里的pair
     * @param: taskDateStream
     * @param: Comparator.comparing(Function.identity())
     */
    private static <T> Stream<T> mergeSortedStream(Collection<Stream<T>> streams, Comparator<T> comparator) {
        final PriorityQueue<Pair<T, Iterator<T>>> priorityQueue =
                new PriorityQueue<>((p1, p2) -> comparator.compare(p1.getKey(), p2.getKey()));
        //初始化优先队列
        priorityQueue.addAll(
                streams.stream().map(s -> {
                    final Iterator<T> iterator = s.iterator();
                    if(iterator.hasNext())
                        return Pair.of(iterator.next(), iterator);
                    else
                        return null;
                })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));

        //创建Stream并返回
        return Stream.generate(() -> {
            final Pair<T, Iterator<T>> head = priorityQueue.poll();
            if(head != null){
                final T item = head.getKey();
                final Iterator<T> iterator = head.getValue();
                if(iterator.hasNext()){
                    //跳过null值，如果null值进入优先队列会影响排序逻辑
                    T next;
                    do {
                        next = iterator.next();
                    } while (next == null && iterator.hasNext());

                    if(next != null)
                        priorityQueue.add(Pair.of(next, iterator));
                }
                return item;
            } else {
                return null;
            }
        }).takeWhile(Objects::nonNull);
    }

}
