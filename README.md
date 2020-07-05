# simple-accumulator
# simple-accumulator
数据摄入测试
 @Autowired
 private DataInTakeService dataInTakeService;
{
        String configId = "intake_config_paraA";
        DataInTakeConfigTool.updateConfig(configId, new DataInTakeConfig() {{
            setConfigId(configId);
            setContentGrok("aaa");
            setSegmentKey("intakeDate");
            setSegmentType(SegmentTypeEnum.STRING.name());
            setStaticLabelsMap(new LinkedHashMap<String, String>() {{
                put("lable1", "v1");
                put("label2", "v2");
                put("groupKey1", "group1");
                put("value", "10000");
                put("intakeDate", "20200703");
            }});
            setLabelKeyList(Collections.singletonList("lable1"));
            setGroupByKeyList(Collections.singletonList("groupKey1"));
            setValueKey("value");
        }});

        MetricsMessageDto metricsMessageDto = new MetricsMessageDto();

        MetricsMessageDto.MessageBody messageBody = new MetricsMessageDto.MessageBody();
        messageBody.setConfigId(configId);
        messageBody.setContent("sadasdsadsadsada");
        metricsMessageDto.setBody(messageBody);

        metricsMessageDto.setFrom("bdp");
        metricsMessageDto.setTopic("para_a");
        metricsMessageDto.setTimestamp(System.currentTimeMillis());

        dataInTakeService.intake(metricsMessageDto);
}

数据钻取测试
@Autowired
private DataDrillService dataDrillService;
{
        String configId = "drill_config_paraA";
        DataDrillConfigTool.updateConfig(configId, new DataDrillConfig() {{
            setConfigId(configId);
            setSegmentKey("intakeDate");
            setLabelKeyList(Collections.singletonList("lable1"));
            setGroupByKeyList(Collections.singletonList("groupKey1"));
        }});

        MetricsDrillDto metricsDrillDto = new MetricsDrillDto();

        metricsDrillDto.setDataDrillConfigId(configId);
        metricsDrillDto.setDataFrom("bdp");
        metricsDrillDto.setDataTopic("para_a");
        metricsDrillDto.setParamJSON(new JSONObject() {{
            put("lable1", "v1");
            put("label2", "v2");
            put("groupKey1", "group1");
            put("intakeDate", "20200703");
        }});

        List<Metrics> metricsList = dataDrillService.drill(metricsDrillDto);
        System.out.println(JSONObject.toJSONString(metricsList));
}
