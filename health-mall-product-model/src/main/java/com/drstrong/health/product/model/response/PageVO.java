package com.drstrong.health.product.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通用的分页信息结果封装对象,作为在Controller层统一的返回使用
 * 持久层通过Spring-data-jpa获得的分页对象，其分页信息采用的是偏移量进行表述
 * 该对象对分页信息采用的是语义化的描述
 * <P>File name : Page.java </P>
 * <P>Author : zhouayanxin </P>
 * <P>Date : 2017-08-21 </P>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageVO<T> implements Serializable {

	/**
	 * 字段或域定义：<code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5351164108869962081L;

	/**
	 * 目前每页的默认记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 页码
	 */
	@ApiModelProperty(value = "页码")
	protected int pageNo = 1;

	/**
	 * 每页记录数
	 */
	@ApiModelProperty("每页记录数")
	protected int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 数据列表
	 */
	protected List<T> result = Collections.emptyList();

	/**
	 * 总记录数
	 */
	@ApiModelProperty("记录总条数")
	protected int totalCount = -1;

	/**
	 * 返回第一页结果的时的总记录数
	 * 用于流式分页
	 */
	@ApiModelProperty("请求第一页时的总记录数")
	protected int offset = 0;

	/**
	 * 偏移量(根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始)
	 */
	private int first;

	/**
	 * 总页数
	 */
	@ApiModelProperty("总页数")
	protected int totalPages = -1;

	/**
	 * 是否还有下一页
	 */
	@ApiModelProperty("是否还有下一页")
	protected boolean hasNext = false;

	/**
	 * 下一页页码
	 */
	@ApiModelProperty("下页的页号,序号从1")
	protected int nextPage;

	/**
	 * 是否已经设置排序字段
	 */
	protected boolean orderSetted;

	/**
	 * 是否还有上一页
	 */
	@ApiModelProperty("是否还有上一页")
	protected boolean hasPre = false;

	/**
	 * 上一页的序号
	 */
	@ApiModelProperty("上页的页号,序号从1开始")
	protected int prePage;

	/**
	 * 正序、倒序
	 */
	protected String order;

	/**
	 * 排序字段
	 */
	protected String orderBy;

	/**
	 * 当前页实际的记录数
	 */
	@ApiModelProperty("当前页实际的记录数")
	protected int numOfElements;

	/**
	 * 构造函数
	 */
	public PageVO() {

	}

	/**
	 * 构造函数
	 */
	protected PageVO(int pageNo, int pageSize, List<T> results, int totalCount, int offset, String order, String orderBy) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.result = results;
		this.totalCount = totalCount;
		this.offset = offset;// TODO:JPA need to implements stream page
		this.first = (this.pageNo - 1) * this.pageSize;
		//////////////// 计算总页数////////////////////////
		if (this.offset > 0 && this.offset > this.totalCount) {
			this.totalCount = this.offset;
		}
		int count = this.totalCount / this.pageSize;
		if (this.totalCount % this.pageSize > 0) {
			count++;
		}
		this.totalPages = count;
		//////////////// 计算总页数////////////////////////
		this.hasNext = (this.pageNo + 1) <= this.totalPages;
		this.nextPage = this.hasNext ? this.pageNo + 1 : this.pageNo;
		this.hasPre = this.pageNo != 1;
		this.prePage = this.hasPre ? this.pageNo - 1 : this.pageNo;
		this.order = order;
		this.orderBy = orderBy;
		this.orderSetted = StringUtils.isNotBlank(this.orderBy);
		this.numOfElements = this.totalCount;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public List<T> getResult() {
		return this.result;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public int getOffset() {
		return this.offset;
	}

	public int getFirst() {
		return this.first;
	}

	public int getTotalPages() {
		return this.totalPages;
	}

	public boolean isHasNext() {
		return this.hasNext;
	}

	public int getNextPage() {
		return this.nextPage;
	}

	public boolean isOrderSetted() {
		return this.orderSetted;
	}

	public boolean isHasPre() {
		return this.hasPre;
	}

	public int getPrePage() {
		return this.prePage;
	}

	public String getOrder() {
		return this.order;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public int getNumOfElements() {
		return this.numOfElements;
	}

	@Override
	public String toString() {
		return "PageVO{" +
				"pageNo=" + this.pageNo +
				", pageSize=" + this.pageSize +
				", result=" + this.result +
				", totalCount=" + this.totalCount +
				", offset=" + this.offset +
				", first=" + this.first +
				", totalPages=" + this.totalPages +
				", hasNext=" + this.hasNext +
				", nextPage=" + this.nextPage +
				", orderSetted=" + this.orderSetted +
				", hasPre=" + this.hasPre +
				", prePage=" + this.prePage +
				", order='" + this.order + '\'' +
				", orderBy='" + this.orderBy + '\'' +
				", numOfElements=" + this.numOfElements +
				'}';
	}

	public static Builder newBuilder() {
		return new Builder();
	}


	public static final class Builder {
		private int pageNo;
		private int pageSize;
		private List<?> result;
		private int totalCount;
		private int offset;
		private String order;
		private String orderBy;

		private Builder() {
		}

		/**
		 * 页码
		 *
		 * @param pageNo
		 * @return
		 */
		public Builder pageNo(int pageNo) {
			this.pageNo = pageNo;
			return this;
		}

		/**
		 * 每页记录数
		 *
		 * @param pageSize
		 * @return
		 */
		public Builder pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		/**
		 * 数据列表
		 *
		 * @param result
		 * @return
		 */
		public Builder result(List<?> result) {
			this.result = result;
			return this;
		}

		/**
		 * 记录总条数
		 *
		 * @param totalCount
		 * @return
		 */
		public Builder totalCount(int totalCount) {
			this.totalCount = totalCount;
			return this;
		}

		/**
		 * 返回第一页结果的时的总记录数 用于流式分页
		 *
		 * @param offset
		 * @return
		 */
		public Builder offset(int offset) {
			this.offset = offset;
			return this;
		}

		/**
		 * 正序、倒序
		 *
		 * @param order
		 * @return
		 */
		public Builder order(String order) {
			this.order = order;
			return this;
		}

		/**
		 * 排序字段
		 *
		 * @param orderBy
		 * @return
		 */
		public Builder orderBy(String orderBy) {
			this.orderBy = orderBy;
			return this;
		}

		@SuppressWarnings("unchecked")
		public <T> PageVO<T> build() {
			return new PageVO<>(this.pageNo, this.pageSize, (List<T>) this.result, this.totalCount, this.offset, this.order, this.orderBy);
		}

		@Override
		public String toString() {
			return "Builder{" +
					"pageNo=" + this.pageNo +
					", pageSize=" + this.pageSize +
					", result=" + this.result +
					", totalCount=" + this.totalCount +
					", offset=" + this.offset +
					", order='" + this.order + '\'' +
					", orderBy='" + this.orderBy + '\'' +
					'}';
		}
	}
}