package com.drstrong.health.product.exception;

import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.IBaseResult;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理器,参照 drcnstong 之前的代码
 *
 * @author liuqiuyi
 * @date 2021/12/9 11:25
 */
@RestControllerAdvice
public class DefaultExceptionAdvice {
	private static final Logger log = LoggerFactory.getLogger(DefaultExceptionAdvice.class);

	public DefaultExceptionAdvice() {
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({IllegalArgumentException.class})
	public <T> ResultVO<T> badRequestException(IllegalArgumentException e) {
		return this.defHandler((String) "参数解析失败", e);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({AccessDeniedException.class})
	public <T> ResultVO<T> badMethodExpressException(AccessDeniedException e) {
		return this.defHandler((String) "没有权限请求当前方法", e);
	}

	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public <T> ResultVO<T> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		return this.defHandler((String) "不支持当前请求方法", e);
	}

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public <T> ResultVO<T> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
		return this.defHandler((String) "不支持当前媒体类型", e);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({SQLException.class})
	public <T> ResultVO<T> handleSQLException(SQLException e) {
		return this.defHandler((String) "服务运行SQLException异常", e);
	}

	@ExceptionHandler({BusinessException.class})
	public <T> ResultVO<T> handBusinessException(BusinessException be) {
		log.warn("业务异常: {}", be.getMessage(), be);
		return ResultVO.failed(be);
	}

	@ExceptionHandler({ValidationException.class})
	public <T> ResultVO<T> handValidationException(ValidationException ve) {
		return this.defHandler((IBaseResult) ResultStatus.PARAM_ERROR, ve);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResultVO<String> handMethodArgumentNotValidException(MethodArgumentNotValidException me) {
		log.error("数据校验出现问题：{}，异常类型：{}", me.getMessage(), me.getClass());
		BindingResult bindingResult = me.getBindingResult();
		Map<String, String> errMap = Maps.newHashMap();
		bindingResult.getFieldErrors().forEach((fieldError) -> {
			String var10000 = (String) errMap.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		log.error("参数错误详情：{}, 错误数：{}", errMap, bindingResult.getFieldErrorCount());
		Set<String> errorSet = new HashSet(errMap.values());
		return ResultVO.of(null, ResultStatus.PARAM_ERROR.getCode(), String.join("\n", errorSet), false);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public ResultVO<String> handMissingServletRequestParameterException(MissingServletRequestParameterException me) {
		return this.defHandler((IBaseResult) ResultStatus.PARAM_ERROR, me);
	}

	@ExceptionHandler({SQLIntegrityConstraintViolationException.class})
	public ResultVO<String> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException me) {
		return this.defHandler((IBaseResult) ResultStatus.PARAM_ERROR, me);
	}

	@ExceptionHandler({ConstraintViolationException.class})
	public ResultVO<String> handConstraintViolationException(ConstraintViolationException e) {
		log.warn("违反约束条件：{}，异常类型：{}", e.getMessage(), e.getClass());
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		Map<String, String> errMap = Maps.newHashMap();
		Iterator var4 = constraintViolations.iterator();

		while (var4.hasNext()) {
			ConstraintViolation<?> constraintViolation = (ConstraintViolation) var4.next();
			errMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
		}

		log.error("参数错误详情：{}, 错误数：{}", errMap, constraintViolations.size());
		Set<String> errorSet = new HashSet<>(errMap.values());
		return ResultVO.of(null, ResultStatus.PARAM_ERROR.getCode(), String.join("\n", errorSet), false);
	}

	@ExceptionHandler({NoHandlerFoundException.class})
	public <T> ResultVO<T> handNoFoundException(NoHandlerFoundException e) {
		return this.defHandler((IBaseResult) ResultStatus.NOT_FOUND, e);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class})
	public <T> ResultVO<T> handleException(Exception e) {
		return this.defHandler("未知异常", e);
	}

	private <T> ResultVO<T> defHandler(String msg, Exception e) {
		log.error(msg, e);
		return ResultVO.failed(msg);
	}

	private <T> ResultVO<T> defHandler(IBaseResult result, Exception e) {
		log.error(result.getMessage(), e);
		return ResultVO.failed(result);
	}
}
