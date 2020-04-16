package group.bison.test.accumulator.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import group.bison.test.accumulator.core.AccumulateModel;
import group.bison.test.accumulator.core.Accumulator;
import group.bison.test.accumulator.tools.PersistTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 * Created by diaobisong on 2020/3/29.
 */
@Slf4j
public class MaxAccumulator<T> extends AbstractAccumulator<T, Object> implements Accumulator<T, Object>, Comparator<Object> {

    @Override
    public int offer(T data) {
        Object parameter = getParameter(data);
        if (parameter == null) {
            return 0;
        }

        List<String> accumulatorKeyList = SimpleAccumulatorFactory.getInstance().getAccumulatorKeys(this, null);
        JSONObject dataJSON = data instanceof JSONObject ? (JSONObject) data : JSON.parseObject(JSON.toJSONString(data));
        String maxKey = accumulatorKeyList.stream().map(accumulatorKey -> String.valueOf(dataJSON.getOrDefault(accumulatorKey, accumulatorKey))).reduce(StringUtils::join).get();

        Object maxVal = PersistTool.getObj(maxKey, null);
        if (compare(maxVal, parameter) < 0) {
            maxVal = parameter;
        }

        PersistTool.setObj(maxKey, maxVal);
        return 1;
    }

    @Override
    public Object accumulate(AccumulateModel<T> model) {
        Object result = null;
        try {
            List<String> accumulatorKeyList = SimpleAccumulatorFactory.getInstance().getAccumulatorKeys(this, model);
            String key = accumulatorKeyList.stream().reduce(StringUtils::join).get();
            result = PersistTool.getObj(key, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public int compare(Object o1, Object o2) {
        return o1 == o2 ? 0 : o1 == null ? -1 : o2 == null ? 1 : o1.hashCode() - o2.hashCode();
    }
}
