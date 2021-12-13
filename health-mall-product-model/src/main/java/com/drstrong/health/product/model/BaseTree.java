package com.drstrong.health.product.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2021/12/9 17:47
 */
@Data
public class BaseTree implements Serializable {
	private static final long serialVersionUID = 814912167792587389L;

	@ApiModelProperty(value = "节点 id")
	private Long id;

	@ApiModelProperty(value = "父节点")
	private Long parentId;

	@ApiModelProperty(value = "子节点")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<? super BaseTree> children = Lists.newArrayList();

	public static <T extends BaseTree> List<T> listToTree(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Lists.newArrayList();
		}
		Set<Long> parentIdSet = list.stream().map(BaseTree::getParentId).collect(Collectors.toSet());
		Set<Long> idSet = list.stream().map(BaseTree::getId).collect(Collectors.toSet());
		parentIdSet.removeAll(idSet);
		//用递归找子。
		return list.stream()
				.filter(tree -> parentIdSet.contains(tree.getParentId()))
				.map(tree -> findChildren(tree, list))
				.collect(Collectors.toList());
	}

	private static <T extends BaseTree> T findChildren(T tree, List<T> list) {
		list.stream()
				.filter(node -> node.getParentId().equals(tree.getId()))
				.map(node -> findChildren(node, list))
				.forEachOrdered(children -> tree.getChildren().add(children));
		return tree;
	}
}
