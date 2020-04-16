package group.bison.test.accumulator.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import group.bison.test.accumulator.core.AccumulateModel;
import group.bison.test.accumulator.core.Accumulator;
import group.bison.test.accumulator.tools.PersistTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by diaobisong on 2020/3/29.
 */
@Slf4j
public class CountAccumulator<T> extends AbstractAccumulator<T, Long> implements Accumulator<T, Long> {

    @Override
    public int offer(T data) {
        Object parameter = getParameter(data);
        if (parameter == null) {
            return 0;
        }

        List<String> accumulatorKeyList = SimpleAccumulatorFactory.getInstance().getAccumulatorKeys(this, null);
        JSONObject dataJSON = data instanceof JSONObject ? (JSONObject) data : JSON.parseObject(JSON.toJSONString(data));
        String countKey = accumulatorKeyList.stream().map(accumulatorKey -> String.valueOf(dataJSON.getOrDefault(accumulatorKey, accumulatorKey))).reduce(StringUtils::join).get();
        PersistTool.setValByIncr(countKey, null, "1");
        return 1;
    }

    @Override
    public Long accumulate(AccumulateModel<T> model) {
        List<String> accumulatorKeyList = SimpleAccumulatorFactory.getInstance().getAccumulatorKeys(this, model);
        String key = accumulatorKeyList.stream().reduce(StringUtils::join).get();
        String val = PersistTool.getVal(key, null);
        return StringUtils.isEmpty(val) ? null : Long.valueOf(val);
    }
}
