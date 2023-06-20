package com.drstrong.health.product.remote.decoder;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author liuqiuyi
 * @date 2023/6/20 15:29
 */
public class MedicineWarehouseBaseDtoDecoder /*implements Decoder*/ {
//    //泛型的classType ,,decode 方法进行手动解析.
//    public final static String genericsHeader = "generics-header";
//
//    private Decoder decoder;
//
//    public CustomDecoder(Decoder decoder) {
//        this.decoder = decoder;
//    }
//
//    @Override
//    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
//        Object returnObject = null;
//
//        if (isParameterizeHttpEntity(type)) {
//            type = ((ParameterizedType) type).getActualTypeArguments()[0];
//            Object decodedObject = decoder.decode(response, type);
//
//            returnObject = createResponse(decodedObject, response);
//        } else if (isHttpEntity(type)) {
//            returnObject = createResponse(null, response);
//        } else {
//            returnObject = decoder.decode(response, type);
//        }
//        //以上是原默认实现,复制过来,为了拿到returnObject
//        if (returnObject != null) {
//            Map<String, Collection<String>> map = response.headers();
//            if (returnObject instanceof ObjectResponse) {
//                Collection<String> list = map.get(genericsHeader);
//                if (list != null) {
//                    Object object = ReflectUtil.newInstance(((LinkedList) list).get(0).toString());
//                    String body = IoUtil.read(response.body().asInputStream(), "UTF-8");
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//                    JsonNode jsonNode = objectMapper.readTree(body);
//                    if (!jsonNode.get("success").booleanValue()) {
//                        return returnObject;
//                    }
//                    //拿出result ,实例化对象(genericsHeader 指定的类型),然后重新set
//                    String result = jsonNode.get("model").toString();
//                    ((ObjectResponse) returnObject).setModel(objectMapper.readValue(result, object.getClass()));
//
//                }
//
//            }
//        }
////        log.info("计算耗时:{}",System.currentTimeMillis()-start);
//
//        return returnObject;
//    }
//
//
//    private boolean isParameterizeHttpEntity(Type type) {
//        if (type instanceof ParameterizedType) {
//            return isHttpEntity(((ParameterizedType) type).getRawType());
//        }
//        return false;
//    }
//
//    private boolean isHttpEntity(Type type) {
//        if (type instanceof Class) {
//            Class c = (Class) type;
//            return HttpEntity.class.isAssignableFrom(c);
//        }
//        return false;
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T> ResponseEntity<T> createResponse(Object instance, Response response) {
//
//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        for (String key : response.headers().keySet()) {
//            headers.put(key, new LinkedList<>(response.headers().get(key)));
//        }
//
//        return new ResponseEntity<>((T) instance, headers, HttpStatus.valueOf(response
//                .status()));
//    }
}
