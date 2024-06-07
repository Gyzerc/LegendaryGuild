package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lang extends FileProvider{
    private LangType type;

    public Lang(LegendaryGuild legendaryGuild) {
        super(legendaryGuild,"./plugins/LegendaryGuild/Lang","Lang/",legendaryGuild.lang.name()+".yml");
    }

    public String plugin;
    public String unknown_command;
    public String nopermission;
    public String already_in_guild;
    public String notmath;
    public String notguild;
    public String notplayer;
    public String nothasguild;
    public String notowner;
    public String notmember;
    public String vault_noenough;
    public String no_guildmoney;
    public String noenough_level;
    public String noenough_treelevel;
    public String noenough_members;
    public String noenough_guildpoints;
    public String nopass_position;
    public String nopass_chance;
    public String no_panel;
    public String input;
    public String isowner;
    public String member_max;
    public List<String> help_player;
    public List<String> help_admin;
    public String uset_recive_points;
    public String uset_decrease_points;
    public String uset_set_points;
    public String user_set_position;
    public String admin_target_nothasguild;
    public String admin_give_points;
    public String admin_take_points;
    public String admin_set_points;
    public String admin_set_position_null;
    public String admin_set_position;
    public String admin_set_position_cant;
    public String admin_set_position_cant_owner;
    public String admin_set_position_cant_max;
    public String admin_give_money;
    public String admin_take_money;
    public String admin_set_money;
    public String admin_level_add;
    public String admin_level_take;
    public String admin_level_set;
    public String admin_exp_add;
    public String admin_exp_take;
    public String admin_exp_set;
    public String admin_treeexp_add;
    public String admin_treeexp_take;
    public String admin_treeexp_set;
    public String admin_treelevel_add;
    public String admin_treelevel_set;
    public String admin_treelevel_take;
    public String admin_bufflevel_add;
    public String admin_bufflevel_take;
    public String admin_bufflevel_set;
    public String admin_buff_null;
    public String admin_activity_add;
    public String admin_activity_take;
    public String admin_activity_set;
    public String admin_maxmembers_add;
    public String admin_maxmembers_set;
    public String admin_teamshop_refresh;
    public String default_null;
    public String default_guild;
    public String default_position;
    public String default_intro;
    public String default_notice;
    public String create_cooldown;
    public String create_length;
    public String create_exists;
    public String create_message;
    public List<String> create_broad;
    public String reuirement_notenough_vault;
    public String reuirement_notenough_playerpoints;
    public String requirement_notenough_item;
    public String requirement_notenough_activity;
    public String requirement_notenough_total_activity;
    public String application_send;
    public String application_recive;
    public String application_already;
    public String application_already_in_guild;
    public String application_wait;
    public String application_pass;
    public String application_join;
    public String application_deny;
    public String application_deny_target;
    public String application_join_broad;
    public String members_kick;
    public String members_kick_broad;
    public String members_bekick;
    public String stores_unlock;
    public String stores_unlock_broad;
    public String stores_has_used;
    public String stores_add_white;
    public String stores_add_white_already;
    public String stores_remove_white;
    public String stores_remove_white_null;
    public String stores_add_white_title;
    public String stores_remove_white_title;
    public String stores_cant_use;
    public String redpacket_min_total;
    public String redpacket_min_amount;
    public String redpacket_create;
    public String redpacket_create_broad;
    public String redpacket_garb_no;
    public String redpacket_garb_already;
    public String redpacket_garb;
    public String redpacket_garb_finally;
    public String redpacket_create_amount_max;
    public String level_levelup;
    public List<String> level_levelup_broad;
    public String level_expadd;
    public String tree_levelup;
    public String tree_levelup_cant;
    public String tree_levelup_byplayer;
    public String tree_level_large;
    public String tree_level_max;
    public String tree_expadd_byplayer;
    public String tree_wish;
    public String tree_wish_broad;
    public String tree_wish_already;
    public String tree_water;
    public String tree_water_broad;
    public String tree_water_limit;
    public String icon_unlock;
    public String icon_locked;
    public String icon_put;
    public String shop_buy;
    public String shop_limit;
    public String buff_levelup;
    public String buff_max;
    public String buff_cant;
    public String pvp_enable;
    public String pvp_disable;
    public String pvp_cant;
    public String pvp_cant_target;
    public String quit_message;
    public String quit_broad;
    public String quit_owner;
    public String delete_confirm;
    public String delete_message;
    public String delete_broad_members;
    public List<String> delete_broad;
    public String give_message;
    public String give_message_target;
    public String give_broad;
    public String activity_gain;
    public String activity_join_tip;
    public String activity_claim;
    public String activity_cant_claim;
    public String activity_already_claimed;
    public String positions_add_write;
    public String positions_remove_write;
    public String positions_max;
    public String positions_message;
    public String positions_message_target;
    public String positions_message_cancel;
    public String positions_message_cancel_target;
    public String money_message;
    public String money_message_broad;
    public String home_home_null;
    public String home_set;
    public String home_set_broad;
    public String home_wait;
    public String home_teleport;
    public String home_cancel;
    public String home_cant_world;
    public String home_cant_server;
    public String intro_add;
    public String intro_remove;
    public String notice_add;
    public String notice_remove;
    public String chat_enable;
    public String chat_disable;
    public String reset_activity;
    public String reset_shop;
    public String reset_wish;
    public String reset_pot;
    public String reset_guild_teamshop;
    public String reset_user_teamshop;
    public String bargain_already;
    public String bargain_success;
    public String bargain_bargain_broad;
    public String bargain_buy;
    public String bargain_buy_broad;
    public String bargain_buy_limit;


    @Override
    public void readDefault() {

        this.plugin = legendaryGuild.color(getValue("plugin","&b&lLegendary&3&lGuild"));
        this.unknown_command = legendaryGuild.color(getValue("unknown_command","&f该指令不存在或格式出错！"));
        this.nopermission = legendaryGuild.color(getValue("nopermission","&f你没有该权限！"));
        this.already_in_guild = legendaryGuild.color(getValue("already_in_guild","&f你已经在一个公会内了"));
        this.nothasguild = legendaryGuild.color(getValue("nothasguild","&f你不在一个公会内！"));
        this.notowner = legendaryGuild.color(getValue("notowner","&f你不是会长！"));
        this.notmember = legendaryGuild.color(getValue("notmember","&f公会内没有该成员"));
        this.no_guildmoney = legendaryGuild.color(getValue("no_guildmoney","&f公会资金不足 &e%value%"));
        this.noenough_level = legendaryGuild.color(getValue("noenough_level","&f公会等级不足 &e%value%"));
        this.noenough_treelevel = legendaryGuild.color(getValue("noenough_treelevel","&f公会神树等级不足 &e%value%"));
        this.noenough_members = legendaryGuild.color(getValue("noenough_members","&f公会成员人数不足 &e%value%"));
        this.noenough_guildpoints = legendaryGuild.color(getValue("noenough_guildpoints","&f你的公会贡献值不足 &e%value%"));
        this.nopass_position = legendaryGuild.color(getValue("nopass_position","&c你的公会职位不允许进行该操作."));
        this.nopass_chance = legendaryGuild.color(getValue("nopass_chance","&c你的运气似乎不太好..."));
        this.no_panel = legendaryGuild.color(getValue("no_panel","&f没有该界面."));
        this.input = legendaryGuild.color(getValue("input","&f请在聊天框中发送内容，输入 &c'cancel' &f即可取消."));
        this.vault_noenough = legendaryGuild.color(getValue("vault_noenough","&f你的游戏币不足 &e%value%"));
        this.member_max = legendaryGuild.color(getValue("member_max","&f公会人数已满，无法再招募更多成员！"));
        this.isowner = legendaryGuild.color(getValue("isowner","&f该玩家是会长！"));

        this.help_player = legendaryGuild.color(getValue("help_player",new ArrayList<>()));
        this.help_admin = legendaryGuild.color(getValue("help_admin",new ArrayList<>()));

        this.notmath = legendaryGuild.color(getValue("notmath","&f请输入正确的数字！"));
        this.notguild = legendaryGuild.color(getValue("notguild","该公会不存在！"));
        this.notplayer = legendaryGuild.color(getValue("notplayer","&f该玩家不存在！"));

        this.uset_recive_points = legendaryGuild.color(getValue("user.recive_points","&f你的公会贡献点增加了 &a%value%"));
        this.uset_decrease_points = legendaryGuild.color(getValue("user.decrease_points","&f你的公会贡献点减少了 &a%value%"));
        this.uset_set_points = legendaryGuild.color(getValue("user.set_points","&f你的公会贡献点被设置为 &a%value%"));
        this.user_set_position = legendaryGuild.color(getValue("user.set_position","&f你的公会职位变化为 &a%value%"));

        this.admin_give_points = legendaryGuild.color(getValue("admin.give_points","&f玩家 &e%target% &f的公会贡献点增加了 &a%value%"));
        this.admin_take_points = legendaryGuild.color(getValue("admin.take_points","&f玩家 &e%target% &f的公会贡献点减少了 &a%value%"));
        this.admin_set_points = legendaryGuild.color(getValue("admin.set_points","&f玩家 &e%target% &f的公会贡献被设置为 &a%value%"));
        this.admin_target_nothasguild = legendaryGuild.color(getValue("admin.target_nothasguild","&f该玩家不在公会内."));
        this.admin_set_position_null = legendaryGuild.color(getValue("admin.set_position_null","&f该职位不存在！"));
        this.admin_set_position = legendaryGuild.color(getValue("admin.set_position","&f玩家 &e%target% &f的职位被设置为 &a%value%"));
        this.admin_set_position_cant = legendaryGuild.color(getValue("admin.set_position_cant","&f该玩家是公会会长，无法变更职位！"));
        this.admin_set_position_cant_owner = legendaryGuild.color(getValue("admin.set_position_cant_owner","&f会长这个职位无法被任命"));
        this.admin_set_position_cant_max = legendaryGuild.color(getValue("admin.set_position_cant_max","&f该公会的该职位人数达到上限.."));
        this.admin_give_money = legendaryGuild.color(getValue("admin.give_money","&f公会 &e%target% &f的资金增加了 &a%value%"));
        this.admin_take_money = legendaryGuild.color(getValue("admin.take_money","&f公会 &e%target% &f的资金减少了 &a%value%"));
        this.admin_set_money = legendaryGuild.color(getValue("admin.set_money","&f公会 &e%target% &f的资金被设置为 &a%value%"));
        this.admin_level_set = legendaryGuild.color(getValue("admin.level_set","&f你将公会 %target% &f的等级设置为 &a%value% &f级"));
        this.admin_level_add = legendaryGuild.color(getValue("admin.level_add","&f你将公会 %target% &f的等级提升了 &a%value% &f级"));
        this.admin_level_take= legendaryGuild.color(getValue("admin.level_take","&f你将公会 %target% &f的等级减少了 &a%value% &f级"));
        this.admin_exp_add = legendaryGuild.color(getValue("admin.exp_add","&f你将公会 &e%target% &f的经验提升了 &e%value%"));
        this.admin_exp_take = legendaryGuild.color(getValue("admin.exp_take","&f你将公会 &e%target% &f的经验减少了 &e%value%"));
        this.admin_exp_set = legendaryGuild.color(getValue("admin.exp_set","&f你将公会 &e%target% &f的经验设置为 &e%value%"));
        this.admin_treelevel_set = legendaryGuild.color(getValue("admin.treelevel_set","&f你将公会 %target% &f的公会神树等级设置为 &a%value% &f级"));
        this.admin_treelevel_add = legendaryGuild.color(getValue("admin.treelevel_add","&f你将公会 %target% &f的公会神树等级提升了 &a%value% &f级"));
        this.admin_treelevel_take= legendaryGuild.color(getValue("admin.treelevel_take","&f你将公会 %target% &f的公会神树等级减少了 &a%value% &f级"));
        this.admin_treeexp_add = legendaryGuild.color(getValue("admin.treeexp_add","&f你将公会 &e%target% &f的公会神树成长值提升了 &e%value%"));
        this.admin_treeexp_take = legendaryGuild.color(getValue("admin.treeexp_take","&f你将公会 &e%target% &f的公会神树成长值减少了 &e%value%"));
        this.admin_treeexp_set = legendaryGuild.color(getValue("admin.treeexp_set","&f你将公会 &e%target% &f的公会神树成长值设置为 &e%value%"));
        this.admin_bufflevel_add = legendaryGuild.color(getValue("admin.bufflevel_add","&f你将公会 &e%target% &f的buff %buff% &f等级提升了 &e%value% &f级！"));
        this.admin_bufflevel_take = legendaryGuild.color(getValue("admin.bufflevel_take","&f你将公会 &e%target% &f的buff %buff% &f等级降低了 &e%value% &f级！"));
        this.admin_bufflevel_set = legendaryGuild.color(getValue("admin.bufflevel_set","&f你将公会 &e%target% &f的buff %buff% &f等级设置为 &e%value% &f级！"));
        this.admin_buff_null = legendaryGuild.color(getValue("admin.buff_null","&f该buff不存在！"));
        this.admin_activity_add = legendaryGuild.color(getValue("admin.activity_add","&f你将公会 &e%target% &f的活跃度提升了 &e%value%"));
        this.admin_activity_take = legendaryGuild.color(getValue("admin.activity_take","&f你将公会 &e%target% &f的活跃度减少了 &e%value%"));
        this.admin_activity_set = legendaryGuild.color(getValue("admin.activity_set","&f你将公会 &e%target% &f的活跃度设置为 &e%value%"));
        this.admin_maxmembers_add = legendaryGuild.color(getValue("admin.maxmembers_add","&fYou have increased the maximum number of members in the guild &e%target% &fby &e%value%"));
        this.admin_maxmembers_set = legendaryGuild.color(getValue("admin.maxmembers_set","&fYou have set the maximum number of members in the guild &e%target% &fby &e%value%"));
        this.admin_teamshop_refresh = legendaryGuild.color(getValue("admin.teamshop-refresh","&fYou have refresh the item of teamshop for guild &e%target%"));

        this.default_null = legendaryGuild.color(getValue("default.null","无"));
        this.default_guild = legendaryGuild.color(getValue("default.guild","无公会"));
        this.default_position = legendaryGuild.color(getValue("default.position","无职位"));
        this.default_intro = legendaryGuild.color(getValue("default.intro","这个公会的会长很懒,还没有留下任何介绍"));
        this.default_notice = legendaryGuild.color(getValue("default.notice","这个公会的会长很懒,还没有留下任何公告"));

        this.create_cooldown = legendaryGuild.color(getValue("create.cooldown","&f你还需等待 &c%value%s &f后才能加入或者创建新的公会"));
        this.create_length = legendaryGuild.color(getValue("create.length","&f该公会名称过长！"));
        this.create_exists = legendaryGuild.color(getValue("create.exists","&f该公会名称已经存在了！"));
        this.create_message = legendaryGuild.color(getValue("create.message","&f你成功创建了公会 &e%value% &f赶快邀请玩家加入你的公会吧！"));
        this.create_broad = legendaryGuild.color(getValue("create.broad",new ArrayList<>()));

        this.reuirement_notenough_vault = legendaryGuild.color(getValue("requirements.notenough_vault","&f你的游戏币不足 &e%value%"));
        this.reuirement_notenough_playerpoints = legendaryGuild.color(getValue("requirements.notenough_playerpoints","&f你的点券不足 &e%value%"));
        this.requirement_notenough_item = legendaryGuild.color(getValue("requirements.notenough_item","&f你的 %item% &f不足 &e%value%个"));
        this.requirement_notenough_activity = legendaryGuild.color(getValue("requirements.notenough_activity","&fYour activity points did not reach &e%value% &fthis week"));
        this.requirement_notenough_total_activity = legendaryGuild.color(getValue("requirements.notenough_total-activity","&fYour history activity points did not reach &e%value%"));

        this.application_send = legendaryGuild.color(getValue("application.send","&f你向 %value% &f发送了入会申请,请等待会长审核."));
        this.application_recive = legendaryGuild.color(getValue("application.recive","&f玩家 &e%player% &f想要加入公会,请及时处理入会申请.."));
        this.application_already = legendaryGuild.color(getValue("application.already","&f你已经向该公会发送过申请了,请等待审核."));
        this.application_already_in_guild = legendaryGuild.color(getValue("application.already_in_guild","&f该玩家已经加入了其他的公会了.."));
        this.application_wait = legendaryGuild.color(getValue("application.wait","&f你目前有 &e%value% &f个入会申请待处理.."));
        this.application_pass = legendaryGuild.color(getValue("application.pass","&f你通过了玩家 &e%value% &f的入会申请."));
        this.application_join = legendaryGuild.color(getValue("application.join","&f你加入了公会 %value%"));
        this.application_join_broad = legendaryGuild.color(getValue("application.join_broad","&f新的成员 &e%value% &f加入了我们的公会！"));
        this.application_deny = legendaryGuild.color(getValue("application.deny","&c你拒绝了 &d%value% &c的入会申请"));
        this.application_deny_target = legendaryGuild.color(getValue("application.deny_target","&c你向公会 %value% &c发送的入会申请被拒绝..."));

        this.members_kick = legendaryGuild.color(getValue("members.kick","&f你将 &e%value% &f踢出了公会."));
        this.members_kick_broad = legendaryGuild.color(getValue("members.kick_broad","&f公会成员 &e%value% &f被踢出了公会."));
        this.members_bekick = legendaryGuild.color(getValue("members.bekick","&f你被 &e%value% &f的会长踢出了公会..."));

        this.stores_unlock = legendaryGuild.color(getValue("stores.unlock","&f你将公会 &E%value%号仓库 &f解锁了！"));
        this.stores_unlock_broad = legendaryGuild.color(getValue("stores.unlock_broad","&f公会解锁了 &e%value%号仓库！"));
        this.stores_has_used = legendaryGuild.color(getValue("stores.has_used","&f该仓库有成员正在使用.."));
        this.stores_add_white = legendaryGuild.color(getValue("stores.add_white","&f你成功对 &e%target% &f开放使用 &e%value%号仓库"));
        this.stores_add_white_already = legendaryGuild.color(getValue("stores.add_white_already","&f该成员已经在该号仓库的白名单内"));
        this.stores_remove_white = legendaryGuild.color(getValue("stores.remove_white","&f你已关闭 &e%target% &f的 &3%value%号仓库 &f使用权"));
        this.stores_remove_white_null = legendaryGuild.color(getValue("stores.remove_white_null","&f该仓库的白名单内没有该成员"));
        this.stores_add_white_title = legendaryGuild.color(getValue("stores.add_white_title","&f请发送玩家名称到聊天栏, 输入 'cancel' 即可取消"));
        this.stores_remove_white_title = legendaryGuild.color(getValue("stores.remove_white_title","&f请发送移除该仓库白名单内的成员名字, 输入 'cancel' 即可取消"));
        this.stores_cant_use = legendaryGuild.color(getValue("stores.cant_use","&f该仓库被设置了权限，你不在该仓库的信任名单内"));

        this.redpacket_min_amount = legendaryGuild.color(getValue("redpacket.min_amount","&f最少红包份数为&e 2个"));
        this.redpacket_min_total = legendaryGuild.color(getValue("redpacket.min_total","&f最低红包金额为 &e100.0"));
        this.redpacket_create = legendaryGuild.color(getValue("redpacket.create","&f你发放了一个公会红包，总金额 &e%total% &f共 &e%amount% &f份."));
        this.redpacket_create_broad = legendaryGuild.color(getValue("redpacket.create_broad","&f公会成员 &e%target% &f发放了总金额为 &e%total% &f的红包，共 &e%amount% &f份！赶快抢吧！"));
        this.redpacket_garb_no = legendaryGuild.color(getValue("redpacket.garb_no","&f你来迟了，该红包已经被瓜分完了..."));
        this.redpacket_garb_already = legendaryGuild.color(getValue("redpacket.garb_already","&f你已经领取过该红包了.."));
        this.redpacket_garb = legendaryGuild.color(getValue("redpacket.garb","&f公会成员 &e%target% &f领取了 &e%value% &f发放的红包，获得了 &e%money%游戏币"));
        this.redpacket_garb_finally = legendaryGuild.color(getValue("redpacket.garb_finally","&f公会成员 &e%target% &f发放的红包已被领取完毕,运气王是 &e%luck% &f金额为 &e%value%"));
        this.redpacket_create_amount_max = legendaryGuild.color(getValue("redpacket.create_amount_max","&f红包数量不能超过公会成员的人数"));

        this.level_levelup = legendaryGuild.color(getValue("level.levelup","'&f公会等级提升！目前等级为 &e%value%"));
        this.level_levelup_broad = legendaryGuild.color(getValue("level.levelup_broad", Arrays.asList(" ","&7[&6公会&7] &f公会 &e%target% &f的等级提升为 &e%value%！"," ")));
        this.level_expadd = legendaryGuild.color(getValue("level.expadd","&f公会经验增加了 &e%value%"));

        this.tree_levelup = legendaryGuild.color(getValue("tree.levelup","&f公会神树等级提示！当前等级为 &e%value%"));
        this.tree_levelup_byplayer = legendaryGuild.color(getValue("tree.levelup_byplayer","&f你成功提升了公会神树的等级！"));
        this.tree_level_large = legendaryGuild.color(getValue("tree.level_large","&f神树等级无法超过公会等级！"));
        this.tree_levelup_cant = legendaryGuild.color(getValue("tree.levelup_cant","公会神树成长值不足！无法升级..."));
        this.tree_level_max = legendaryGuild.color(getValue("tree.level_max","&f神树等级已达到上限."));
        this.tree_expadd_byplayer = legendaryGuild.color(getValue("tree.expadd_byplayer","&f公会神树成长值提升了 &a%value%"));
        this.tree_wish = legendaryGuild.color(getValue("tree.wish","&a许愿成功！获得神树的馈赠！"));
        this.tree_wish_broad = legendaryGuild.color(getValue("tree.wish_broad","&f公会成员 &e%target% &f今日完成神树许愿并获得了神树的馈赠！"));
        this.tree_wish_already = legendaryGuild.color(getValue("tree.wish_already","&f你今日已经许愿过了.."));
        this.tree_water = legendaryGuild.color(getValue("tree.water","&f你成功使用 %value% &f为神树浇灌！"));
        this.tree_water_broad = legendaryGuild.color(getValue("tree.water_broad","&f公会成员使用 %value% &f为神树进行了浇灌."));
        this.tree_water_limit = legendaryGuild.color(getValue("tree.water_limit","&f你今日不能再使用该水壶了！"));

        this.icon_unlock = legendaryGuild.color(getValue("icon.unlock","&f你为公会解锁了公会图标 %value%"));
        this.icon_locked = legendaryGuild.color(getValue("icon.locked","&f该图标还未解锁"));
        this.icon_put = legendaryGuild.color(getValue("icon.put","&f你将公会图标更改为 %value%"));

        this.shop_buy = legendaryGuild.color(getValue("shop.buy","&f你购买了 %value%"));
        this.shop_limit = legendaryGuild.color(getValue("shop.limit","&f你不能再购买该商品了..."));

        this.buff_levelup = legendaryGuild.color(getValue("buff.levelup","&d公会buff %target% &d等级提升,目前为 &e%value% &d级."));
        this.buff_max = legendaryGuild.color(getValue("buff.max","&c该Buff的等级已经达到上限."));
        this.buff_cant = legendaryGuild.color(getValue("buff.cant","&cBuff的等级不能超过公会等级."));

        this.pvp_cant = legendaryGuild.color(getValue("pvp.cant","&f你已开启公会保护模式,无法攻击同一个公会的玩家！"));
        this.pvp_cant_target = legendaryGuild.color(getValue("pvp.cant_target","&f对方已开启公会保护模式，无法对其造成伤害！"));
        this.pvp_enable = legendaryGuild.color(getValue("pvp.enable","&c你已关闭公会保护模式，现在将会对同一个公会的玩家造成伤害."));
        this.pvp_disable = legendaryGuild.color(getValue("pvp.disable","&f你已开启公会保护模式,无法攻击同一个公会的玩家！"));

        this.quit_message = legendaryGuild.color(getValue("quit.message","&4你退出了公会 %value%"));
        this.quit_broad = legendaryGuild.color(getValue("quit.broad","&c公会成员 %position%%value% &c退出了公会...."));
        this.quit_owner = legendaryGuild.color(getValue("quit.owner","&f你的公会会长，你只能&e转让、解散&f公会."));


        this.delete_confirm = legendaryGuild.color(getValue("delete.confirm","&4你确定要解散公会吗？请使用指令 &e/guild delete confirm &4以解散公会."));
        this.delete_message = legendaryGuild.color(getValue("delete.message","&7你解散了 %value%"));
        this.delete_broad_members = legendaryGuild.color(getValue("delete.broad_members","&c你所在的公会已被解散..."));
        this.delete_broad = legendaryGuild.color(getValue("delete.broad",Arrays.asList("","&7[&6公会&7] &C公会 &e%value% &C已被解散了..","")));

        this.give_message = legendaryGuild.color(getValue("give.message","&c你将公会 %value% &c转让给了 &e%target%"));
        this.give_message_target = legendaryGuild.color(getValue("give.message_target","&6原公会会长已将公会 %value% &6转让给你了."));
        this.give_broad = legendaryGuild.color(getValue("give.broad","&e新的会长 &a%value% &e上任，原会长已卸甲归田..."));

        this.activity_gain = legendaryGuild.color(getValue("activity.gain","&a公会活跃度提升了 &e%value%"));
        this.activity_join_tip = legendaryGuild.color(getValue("activity.join_tip","&a你有未领取的公会活跃度奖励."));
        this.activity_claim = legendaryGuild.color(getValue("activity.claim","&e你领取了 %value%"));
        this.activity_cant_claim = legendaryGuild.color(getValue("activity.cant_claim","&c当前公会活跃度不足 &e%value%"));
        this.activity_already_claimed = legendaryGuild.color(getValue("activity.already_claimed","&c你已经领取过该活跃度奖励了."));

        this.positions_add_write = legendaryGuild.color(getValue("positions.add_write","&f请输入成员名称以任命 %position% &f输入 &c'cancel' &f取消"));
        this.positions_remove_write = legendaryGuild.color(getValue("positions.remove_write","&f请输入成员名称以取消任命 %position% &f输入 &c'cancel' &f取消"));
        this.positions_max = legendaryGuild.color(getValue("positions.max","&f该职位当前在位人数已经达到上限.."));
        this.positions_message = legendaryGuild.color(getValue("positions.message","&e你任命成员 &a%target% &e为 %value%"));
        this.positions_message_target = legendaryGuild.color(getValue("positions.message_target","&a你被公会会长任命为 %value%"));
        this.positions_message_cancel= legendaryGuild.color(getValue("positions.message_cancel","&c你将 &f%value% &c的职位取消了."));
        this.positions_message_cancel_target= legendaryGuild.color(getValue("positions.message_cancel_target","&c你的职位已被取消."));

        this.money_message= legendaryGuild.color(getValue("money.message","&a你向公会捐赠了 &e%value%"));
        this.money_message_broad= legendaryGuild.color(getValue("money.message_broad","&b公会成员 &f%target% &b向公会捐赠了 &f%value% &b资金."));

        this.home_home_null= legendaryGuild.color(getValue("home.home_null","&c公会还未设置驻地."));
        this.home_set= legendaryGuild.color(getValue("home.set","&e你将公会驻地更改到当前位置."));
        this.home_set_broad= legendaryGuild.color(getValue("home.set_broad","&c公会驻地已被更改."));
        this.home_wait= legendaryGuild.color(getValue("home.wait","&e请等待 &a%value%s &e后返回公会驻地,在此期间请不要移动.."));
        this.home_teleport= legendaryGuild.color(getValue("home.teleport","&a已传送至公会驻地."));
        this.home_cancel= legendaryGuild.color(getValue("home.cancel","&c你取消了本次传送."));
        this.home_cant_world= legendaryGuild.color(getValue("home.cant_world","&c该世界禁止设置驻地"));
        this.home_cant_server= legendaryGuild.color(getValue("home.cant_server","&c该大区禁止设置驻地"));

        this.intro_add= legendaryGuild.color(getValue("intro.add","&a你添加了一条新的公会介绍：&f%value%"));
        this.intro_remove= legendaryGuild.color(getValue("intro.remove","&c你移除了最新后一条公会介绍：&f%value%"));
        this.notice_add= legendaryGuild.color(getValue("notice.add","&a你添加了一条新的公会公告：&f%value%"));
        this.notice_remove= legendaryGuild.color(getValue("notice.remove","&c你移除了最新后一条公会公告：&f%value%"));

        this.chat_enable = legendaryGuild.color(getValue("chat.enable","&a你已开启公会聊天,接下来的消息仅会被公会成员所看见."));
        this.chat_disable = legendaryGuild.color(getValue("chat.disable","&c你关闭了公会聊天,接下的消息将被全体玩家所看见."));

        this.reset_activity = legendaryGuild.color(getValue("reset.activity","&cYou have reset the guild activity data for &f%guild%"));
        this.reset_shop = legendaryGuild.color(getValue("reset.shop","&cYou have reset the player %type% shop date for &e%player%"));
        this.reset_wish = legendaryGuild.color(getValue("reset.wish","&cYou have reset the guild tree wish data for &e%player%"));
        this.reset_pot = legendaryGuild.color(getValue("reset.pot","&cYou have reset the guild tree water data for &e%player% &c, id &e%pot%"));
        this.reset_guild_teamshop = legendaryGuild.color(getValue("reset.guild-teamshop","&cYou have reset the guild teamshop data for the members of &f%guild%"));
        this.reset_user_teamshop = legendaryGuild.color(getValue("reset.user-teamshop","&cYou have reset the guild team shop data to &e%amount% &cfor &e%player%"));

        this.bargain_already = legendaryGuild.color(getValue("bargain.already","&cYou have already discounted the price."));
        this.bargain_success = legendaryGuild.color(getValue("bargain.success","&aYou have successfully negotiated a price of &e%bargain%, &aand the current group purchase price is &e%current%"));
        this.bargain_bargain_broad = legendaryGuild.color(getValue("bargain.bargain-broad","&6Guild members &f%player% &6slashed the price of group buying products, resulting in a decrease of &a%bargain% &6.Currently, the price of group buying products is &a%current%"));
        this.bargain_buy = legendaryGuild.color(getValue("bargain.buy","&aYou have successfully purchased the guild group buying gift pack %display%"));
        this.bargain_buy_broad = legendaryGuild.color(getValue("bargain.buy-broad","&6Guild members &f%player% &6purchased the guild group buying gift pack %display% &6at a price of &a%price%&6"));
        this.bargain_buy_limit = legendaryGuild.color(getValue("bargain.buy-limit","&cYou have already purchased this gift pack &f%limit% &ctimes today and cannot make any further purchases！"));
    }

    public enum LangType {
        Chinese,English
    }
}
