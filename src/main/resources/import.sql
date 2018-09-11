INSERT INTO `user` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `account`, `admin`, `avatar`, `deleted`, `nickname`, `password`, `salt`, `state`) VALUES (1, NULL, '2018-08-28 20:09:46', NULL, '2018-08-28 20:09:46', 1, 'admin', b'1', '/static/user/admin.png', b'0', '管理员', '0739a3369c10164836bbda99788b621d', 'db56539e2484c7e9fc518e3d9a7b8e4e', 1);
INSERT INTO `user` (`id`,  `create_by`, `create_date`, `update_by`, `update_date`, `version`, `account`, `admin`, `avatar`, `deleted`, `nickname`, `password`, `salt`, `state`) VALUES (2, NULL, '2018-09-01 20:55:06', NULL, '2018-09-01 20:55:06', 1, 'test', b'0', '/static/user/user_4.png', b'0', '测试', '713bfa4e379c765bc40f8e49b9fdb572', '1890fa04d9af7dc0028b9588d88f082d', 1);

INSERT INTO `sys_role` (`id`, `available`,`description`,`role`) VALUES (1, 0,'管理员','admin');
INSERT INTO `sys_role` (`id`, `available`,`description`,`role`) VALUES (2, 1,'普通会员','ordinary');

INSERT INTO `sys_user_role` (`role_id`,`id`) VALUES (1,1);
INSERT INTO `sys_user_role` (`role_id`,`id`) VALUES (2,2);

INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (1, '推荐', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (2, '附近', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (3, '理财', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (4, '生活', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (5, '健康', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (6, '购物', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (7, '新时代', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (8, '科技', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (9, '时尚', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (10, '电影', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (11, '娱乐', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (12, '财经', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (13, '要闻', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (14, 'NBA', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (15, '股票', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (16, '国际', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (17, '军事', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (18, '体育', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (19, '军事', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (20, '美食', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (21, '法制', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (22, '汽车', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (23, '历史', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (24, '数码', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (25, '游戏', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (26, '故事', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (27, '搞笑', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (28, '家具', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (29, '星座', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (30, '动漫', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (31, '养生', 1,sysdate());
INSERT INTO `category` (`id`, `category_name`, `create_by`, `create_date`) VALUES (32, '手机', 1,sysdate());

INSERT INTO `user_category` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `category_id`, `user_id`) VALUES (1, NULL, '2018-09-02 11:45:11', NULL, '2018-09-02 11:45:11', 1, 1, 1);

INSERT INTO `article_body3` (`id`, `content`) VALUES (1, '<style> .article-box {     min-height: 200px; } .article-box .article-content, .article-box .article-content>div {     font-size: 16px;     line-height: 28px;     color: #222;     word-wrap: break-word; } .article-box .article-content {     margin-bottom: 24px; } </style> <div class="article-box"><div class="article-content"><p>2018-09-05 15:01 | 浙江新闻客户端 通讯员 张玲玲</p><p>9月3日，临海交警汛桥中队在管辖路段执勤时，看到一个小男孩哭泣着独自在马路上行走，执勤队员叶晓俊立即上前了解情况。经了解，小男孩想父母了，出门寻找父母，结果自己迷路了。</p><p>“叔叔叔叔 我爸妈走丢了怎么办……”</p><p>“别怕别怕 叔叔抱你找爸爸妈妈呢”</p><img src="http://p3.pstatp.com/large/pgc-image/1536131922152154b24be3d" img_width="600" img_height="800" alt="“警察叔叔，我爸爸妈妈走丢了”……" inline="0"><p>汛桥中队副指导员翁志慧迅速组织人员在附近寻找，并向110指挥中心说明情况。翁志慧耐心与孩子进行交流，询问父母的电话号码，但因孩子年龄太小，又是外地小朋友，普通话言语模糊，无法获得任何有效信息。天气炎热，叶晓俊一边给孩子喂水，一边安抚孩子不再哭泣。</p><img src="http://p99.pstatp.com/large/pgc-image/15361319224552e11e2f0dd" img_width="720" img_height="510" alt="“警察叔叔，我爸爸妈妈走丢了”……" inline="0"><p>三十分钟后，执勤队员毛武家发现了焦急的家长，带其去见孩子。看到了孩子后，孩子父亲那份慌乱的心也随之消散，上前紧紧抱住孩子，经确认后翁志慧将小男孩交给孩子父亲，离开时孩子父亲对临海交警表示了由衷的感谢。</p><p>在此也要提醒各位家长,以下四个基本技能必须让孩子掌握：</p><p>第一，教孩子记住自己的居住地。教孩子不仅要记住自己和父母的名字，还要让孩子记住自己住的城市名字以及小区名字和门牌号。</p><p>第二，教孩子熟记亲人的电话。让孩子熟记亲人的联系方式，尤其是爸爸妈妈和家里的电话，同时还要教会孩子如何拨打电话。</p><p>第三，教孩子拨打紧急电话号码。应该教会孩子拨打110求助电话，119火警电话，120救护电话。紧急情况，父母电话长容易忘记，也许这些电话在必要的时候能用上。不过一定要记住告诉孩子紧急的时候才能拨打，平时不能乱拨，给人家添乱也不好。</p><p>第四，教孩子识别好人和坏人。不能因一块糖，一个玩具被人带走。警察叔叔、阿姨是家人以外，值得相信的人。</p></div>');

INSERT INTO `article_thumbs_down_count` (`id`, `thumbs_down_count`) VALUES (1, 0);
INSERT INTO `article_thumbs_up_count` (`id`, `thumbs_up_count`) VALUES (1, 0);
INSERT INTO `article_view_count` (`id`, `view_count`) VALUES (1, 0);
INSERT INTO `comment_count` (`id`, `comment_count`) VALUES (1, 0);

INSERT INTO `article` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `article_type`, `privilege`, `title`, `weight`, `article_body1_id`, `article_body3_id`, `comments_count_id`, `thumbs_down_count_id`, `thumbs_up_count_id`, `article_video_body_id`, `view_count_id`, `author_user_id`, `location_id`) VALUES (1, NULL, '2018-09-05 21:46:31', NULL, '2018-09-05 21:46:31', 1, 1, 1, '“警察叔叔，我爸爸妈妈走丢了”……', 2, NULL, 1, 1, 1, 1, NULL, 1, 2, NULL);

INSERT INTO `article_category` (`article_id`, `category_id`) VALUES (1, 4);


INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (1, NULL, NULL, NULL, NULL, 1, '不想结婚', 2);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (2, NULL, NULL, NULL, NULL, 1, '苏州', 2);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (3, NULL, NULL, NULL, NULL, 1, '苏州小学', 2);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (4, NULL, NULL, NULL, NULL, 1, '滴滴最新消息', 2);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (5, NULL, '2018-09-02 00:28:22', NULL, '2018-09-02 00:28:22', 1, '菜谱', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (6, NULL, '2018-09-02 00:30:22', NULL, '2018-09-02 00:30:22', 1, '亚运会金牌榜', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (7, NULL, '2018-09-02 00:32:09', NULL, '2018-09-02 00:32:09', 1, '我的世界动画', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (8, NULL, '2018-09-02 00:33:00', NULL, '2018-09-02 00:33:00', 1, '火灾致19死', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (9, NULL, '2018-09-02 00:33:19', NULL, '2018-09-02 00:33:19', 1, '浙江暂停顺风车', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (10, NULL, '2018-09-02 00:33:34', NULL, '2018-09-02 00:33:34', 1, '全国菜价集体上涨', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (11, NULL, '2018-09-02 00:34:04', NULL, '2018-09-02 00:34:04', 1, '23省平均工资', 1);
INSERT INTO `user_search_history` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `content`, `user_id`) VALUES (12, NULL, '2018-09-02 00:34:33', NULL, '2018-09-02 00:34:33', 1, '国产航母二次试海', 1);

INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (1, 1, '菜谱');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (2, 1, '亚运会');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (3, 1, '金牌榜');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (4, 1, '世界');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (5, 1, '动画');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (6, 2, '火灾');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (7, 1, '浙江');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (8, 1, '暂停');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (9, 1, '风车');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (10, 1, '全国');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (11, 1, '菜价');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (12, 1, '集体');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (13, 1, '上涨');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (14, 1, '平均工资');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (15, 1, '国产');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (16, 1, '航母');
INSERT INTO `search_key_word` (`id`, `count`, `word`) VALUES (17, 1, '二次');

INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (1, 5);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (2, 6);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (3, 7);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (4, 8);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (5, 9);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (6, 10);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (7, 11);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (8, 12);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (9, 13);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (10, 14);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (11, 15);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (12, 16);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (13, 17);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (14, 18);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (15, 19);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (16, 20);
INSERT INTO `search_history_keywords` (`skw_id`, `sh_id`) VALUES (17, 21);