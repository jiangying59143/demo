INSERT INTO `user` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `account`, `admin`, `avatar`, `deleted`, `nickname`, `password`, `salt`, `state`) VALUES (1, NULL, '2018-08-28 20:09:46', NULL, '2018-08-28 20:09:46', 1, 'admin', b'1', '/static/user/admin.png', b'0', '管理员', '0739a3369c10164836bbda99788b621d', 'db56539e2484c7e9fc518e3d9a7b8e4e', 1);
INSERT INTO `user` (`id`, `create_by`, `create_date`, `update_by`, `update_date`, `version`, `account`, `admin`, `avatar`, `deleted`, `nickname`, `password`, `salt`, `state`) VALUES (15, NULL, NULL, NULL, NULL, 1, 'jy', b'0', '/static/user/user_6.png', b'0', 'jiangying', 'c70d21461e3f303e8242fd2cba4fc659', 'a7f78fbe787b99e7bb6c32f9168f28fb', 1);

INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (1,0,'管理员','admin');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (2,1,'普通会员','ordinary');

INSERT INTO `sys_user_role` (`role_id`,`id`) VALUES (1,1);

INSERT INTO `category` (`id`, `categoryname`, `description`) VALUES (1, '前端', NULL);
INSERT INTO `category` (`id`, `categoryname`, `description`) VALUES (2, '后端', NULL);
INSERT INTO `category` (`id`, `categoryname`, `description`) VALUES (3, '生活', NULL);
INSERT INTO `category` (`id`, `categoryname`, `description`) VALUES (4, '数据库', NULL);
INSERT INTO `category` (`id`, `categoryname`, `description`) VALUES (5, '编程语言', NULL);

INSERT INTO `article` (`id`, privilege, article_type, `create_date`, `weight`, `author_user_id`,  `category_id`, `summary`, `title`) VALUES (1, 1, 2, sysdate(), 0, 1, 1, '7月以来6省份党委组织部长调整：辽宁王正谱任四川省委常委', '7月以来6省份党委组织部长调整：辽宁王正谱任四川省委常委');
INSERT INTO `article` (`id`, privilege, article_type, `create_date`, `weight`, `author_user_id`,  `category_id`, `summary`, `title`) VALUES (2, 1, 2, sysdate(), 0, 1, 2, 'summary2', 'title2');
INSERT INTO `article` (`id`, privilege, article_type, `create_date`, `weight`, `author_user_id`,  `category_id`, `summary`, `title`) VALUES (3, 1, 2, sysdate(), 0, 1, 3, 'summary3', 'title3');

INSERT INTO `article_body` (`id`, `article_id`, `content`) VALUES (1, 1, '<p>又有一省份出现省级组织部部长调整。</p><p>据川报观察客户端8月30日消息，近日，经中共中央批准：王正谱同志任中共四川省委委员、常委。</p><img src="http://localhost:8888/image/1/1/1/1.jpg" img_width="550" img_height="338" alt="7月以来6省份党委组织部长调整：辽宁王正谱任四川省委常委" inline="0"><p>王正谱 资料图</p><p>王正谱，男，汉族，1963年8月生，山东烟台人。1987年7月参加工作，1987年4月加入中国共产党，大学学历，学士学位，高级经济师。 </p><p>王正谱是中组部2010年组织央地交流官员时，66名下派的京官之一。他曾任原农业部办公厅副主任、巡视员，农业部新闻发言人，原农业部财务司司长等职，2010年10月出任辽宁省辽阳市委副书记，市政府副市长、代市长 ，之后历任辽阳市长、辽阳市委书记，2015年10月任辽宁省委组织部副部长，2016年6月任省委组织部常务副部长，并于当年10月出任辽宁省委常委、组织部部长。</p><p>今年7月以来，接连6个省份出现省级组织部部长调整。</p><p>7月2日，十九届中央候补委员、湖北省委常委、组织部部长于绍良转任上海市委常委、组织部部长，填补履新吉林省委常委、常务副省长的吴靖平留下的职缺。</p><p>7月3日，2010年下派“京官”之一、四川省委常委、组织部部长黄建发转任浙江省委常委、组织部部长，接替任振鹤转任浙江省委常委、省纪委书记后留下的职缺。</p><p>7月6日，据安徽日报客户端消息，日前中央决定：严植婵同志不再担任安徽省委常委、委员职务，另有任用。严植婵此前担任安徽省委常委、组织部部长，现已出任广西壮族自治区党委常委、副主席。</p><p>此外，8月28日，中共广西壮族自治区委员会发布的王可同志免职的通知显示，经中共中央组织部同意，自治区党委研究决定：免去王可同志的自治区党委组织部部长职务。 </p>');
INSERT INTO `article_body` (`id`, `article_id`, `content`) VALUES (2, 2, 'content2');
INSERT INTO `article_body` (`id`, `article_id`, `content`) VALUES (3, 3, 'content3');