package tools;

/**
 * 生产数据操作层代码
 * 
 * @author hbm
 *
 */
public class GenMainMy {
	public static void main(String[] args) {
		String configFile = "/generatorConfig.xml";
		try {
            // t_wine_channel_request_log t_authority_config
            String[] tableNames = new String[] {"t_adjust_order_log","t_app_config",
"t_daily_paycenter",
"t_daily_task",
"t_tonglian_user",
"t_wine_channel_request_log",
"t_wine_recharge_order",
"t_wine_withdraw_order","t_inout_order_message","t_balance_modify_message","t_withdraw_balancemodify"
};
			GenMybatisFiles.gen(configFile, tableNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
