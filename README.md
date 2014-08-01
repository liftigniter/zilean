Spark Job Manager
==============

`workflow.json` in `resources` folder

```json
[
    {
        "property" : "wikihow",
        "start" : "27-07-2014 2100",
        "end" : "27-08-2015 2100",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.WikiHowUpdateInventory wikihow"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics wikihow {DATE}",
                "datasets" : "activities/wikihow/{DATE}/*"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats wikihow {DATE}",
                "datasets" : "doc_metrics/daily/wikihow/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats wikihow {DATE}",
                "datasets" : "doc_metrics/daily/wikihow/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats wikihow {DATE}",
                "datasets" : "activities/wikihow/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "coderwall",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics coderwall {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats coderwall {DATE}",
                "datasets" : "doc_metrics/daily/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats coderwall {DATE}",
                "datasets" : "doc_metrics/daily/coderwall/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats coderwall {DATE}",
                "datasets" : "activities/coderwall/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "hypster",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics hypster {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats hypster {DATE}",
                "datasets" : "doc_metrics/daily/hypster/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats hypster {DATE}",
                "datasets" : "doc_metrics/daily/hypster/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats hypster {DATE}",
                "datasets" : "activities/hypster/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "findly-sears",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics findly-sears {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats findly-sears {DATE}",
                "datasets" : "doc_metrics/daily/findly-sears/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats findly-sears {DATE}",
                "datasets" : "doc_metrics/daily/findly-sears/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats findly-sears {DATE}",
                "datasets" : "activities/findly-sears/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "teespring",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics teespring {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats teespring {DATE}",
                "datasets" : "doc_metrics/daily/teespring/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats teespring {DATE}",
                "datasets" : "doc_metrics/daily/teespring/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats teespring {DATE}",
                "datasets" : "activities/teespring/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "listia",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics listia {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats listia {DATE}",
                "datasets" : "doc_metrics/daily/listia/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats listia {DATE}",
                "datasets" : "doc_metrics/daily/listia/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats listia {DATE}",
                "datasets" : "activities/listia/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "twitch",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics twitch {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats twitch {DATE}",
                "datasets" : "doc_metrics/daily/twitch/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats twitch {DATE}",
                "datasets" : "doc_metrics/daily/twitch/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats twitch {DATE}",
                "datasets" : "activities/twitch/{-6,0}/*"
            }
        ]
    },
    {
        "property" : "about",
        "start" : "27-07-2014 0300",
        "end" : "27-08-2015 0300",
        "repeat" : 1440,
        "expires" : 2880,
        "actions" : [
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.ActivityToDocMetrics about {DATE}",
                "datasets" : "new_activities/coderwall/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.DailyOverviewStats about {DATE}",
                "datasets" : "doc_metrics/daily/about/{DATE}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysOverviewStats about {DATE}",
                "datasets" : "doc_metrics/daily/about/{-6,0}/_SUCCESS"
            },
            {
                "command" : "java -Dspark.local.dir=/mnt/spark,/mnt2/spark -Dspark-cores-max=18 -Dspark.executor.memory=6144m -cp /root/myjars/api-pipeline-assembly.jar com.petametrics.api.pipeline.jobs.LastXDaysLeaderBoardStats about {DATE}",
                "datasets" : "activities/about/{-6,0}/*"
            }
        ]
    }
]
```