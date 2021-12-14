package com.drstrong.health.product.model.response;

//import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
@Getter
@Setter
@ToString
public class PageVO<T> implements Serializable {

	private static final long serialVersionUID = -8972144119925214882L;

	/**
	 * 总记录数
	 */
	private long totalCount;
	/**
	 * 每页记录数
	 */
	private long pageSize;
	/**
	 * 总页数
	 */
	private long totalPage;
	/**
	 * 当前页数
	 */
	private long currPage;

	/**
	 * 列表数据
	 */
	private List<T> list;

	/**
	 * 分页
	 *
	 * @param list       列表数据
	 * @param totalCount 总记录数
	 * @param pageSize   每页记录数
	 * @param currPage   当前页数
	 */
	private PageVO(List<T> list, Long totalCount, Long pageSize, Long currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		Double totalPage = Math.ceil((double) totalCount / pageSize);
		this.totalPage = totalPage.longValue();
	}

//    public static <T> PageVO<T> toPageVo(IPage<T> page) {
//        return PageVO.toPageVo(page.getRecords(), page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
//    }

	public static <T> PageVO<T> toPageVo(List<T> list, Long totalCount, Long pageSize, Long currPage) {
		return new PageVO<T>(list, totalCount, pageSize, currPage);
	}

	public static <T> PageVO<T> emptyPageVo(Long currPage, Long pageSize) {
		return new PageVO<T>(null, 0L, pageSize, currPage);
	}
}
