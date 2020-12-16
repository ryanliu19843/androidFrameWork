package com.mdx.framework.utility.tools;

import java.util.HashMap;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.UnitConver;

public class NetStatistics {
	public static HashMap<String, Statistics> statisticsMap = new HashMap<String, Statistics>();
	public static final String STATISTICS_BROADCAST_ACTION = "statistics_broadcast_action";

	public static void addStatistics(String id, long up, long down) {
		if (!ToolsConfig.checkStatistics(id)) {
			return;
		}
		if (ToolsConfig.getStatisticsShowType() == 1) {
			statisticsMap.clear();
		}
		Statistics sta = null;
		if (statisticsMap.containsKey(id)) {
			sta = statisticsMap.get(id);
		}
		if (sta == null) {
			sta = new Statistics();
			sta.id = id;
		}
		sta.up += up;
		sta.down += down;
		statisticsMap.put(id, sta);
		log();
	}


	public static void log() {
		StringBuffer sb = new StringBuffer();
		for (String key : statisticsMap.keySet()) {
			Statistics sta = statisticsMap.get(key);
			if (sta != null) {
				sb.append(sta.toString() + "\n");
				sta.print();
			}
		}
		if (ToolsConfig.isLogToolsShow()) {
			BroadCast.PostBroad(new BIntent(STATISTICS_BROADCAST_ACTION, "", null, 0, sb));
		}
	}

	public static class Statistics {
		public String id;
		public long up = 0, down = 0;

		public String toString() {
			return id + "[" + "U:" + UnitConver.getBytesSize(up) + " D:" + UnitConver.getBytesSize(down) + "]";
		}

		public void print() {
			MLog.I(MLog.SYS_RUN, toString());
		}
	}
}
