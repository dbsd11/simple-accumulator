package group.bison.test.data_holdall.drill.domain.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by diaobisong on 2020/7/5.
 */
@AllArgsConstructor
@Getter
public class DataDrillContext {

    private DataDrillConfig dataDrillConfig;

    private JSONObject paramJSON;

    private String dataTopic;

    private String dataFrom;

    private String timestampRange;
}
