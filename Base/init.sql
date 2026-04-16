/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : jxsx

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 04/05/2025 22:15:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_announcement
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement`;
CREATE TABLE `t_announcement`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `image_src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `importanted` bit(1) NULL DEFAULT NULL,
  `overhead` bit(1) NULL DEFAULT NULL,
  `announcement_archive_id` int NULL DEFAULT NULL COMMENT '分类',
  `create_department_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_announcement
-- ----------------------------
INSERT INTO `t_announcement` VALUES (1, '关于启用新的考试系统的通知公告', '<p>各位同学：</p><p>根据学校教务处的安排，为了更好地组织和管理考试工作，提高考试质量，我校将于近期启用新的考试系统。特此通知相关事项如下：</p><ol><li><p>考试系统介绍：新的考试系统将提供稳定、高效、安全的在线考试环境，利用先进的技术手段确保考试的公平性和准确性。同时，系统将提供更多种类的考试题型，以适应不同课程的考核需求。</p></li><li><p>考试系统登录：请同学们使用个人学号和密码登录学校官方网站，在考试系统入口处进行登录操作。登录信息请妥善保管，谨防泄露。</p></li><li><p>考试时间与地点：新的考试系统将按照教务处统一安排的考试时间进行考试，具体的考试时间和地点请同学们密切关注学校官方网站的通知，及时掌握相关信息。</p></li><li><p>考试注意事项：</p><ul><li>在考试前，请同学们提前进行系统测试，确保设备和网络连接正常，以免影响考试进行。</li><li>考试过程中，请同学们保持良好的考试纪律，严禁违规行为，如舞弊、抄袭等，一经发现将会受到相应的处罚。</li><li>考试期间，请同学们保持手机静音或关闭，不得使用通讯工具或查阅与考试无关的资料。</li><li>考试结束后，请同学们及时提交答卷，并按照要求退出考试系统。</li></ul></li></ol><p>我们相信，新的考试系统将为大家提供更好的考试环境和体验，有助于提高学业水平。希望同学们积极配合，严格遵守考试规则，共同营造公平、公正的考试氛围。</p><p>最后，祝愿各位同学在即将到来的考试中取得优异成绩！</p><p>特此通知。</p><p style=\"text-align:right;\">教务处</p><p style=\"text-align:right;\">&nbsp;日期：2023年12月14日</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload/announcement/2023/12/14/cadf21bf-44f2-4ed7-956a-8e4fcfa216c7/212.jpg', b'0', b'0', 1, 1);
INSERT INTO `t_announcement` VALUES (2, '关于严明2025年廉洁纪律的通知', '<h1 style=\"text-align:left;\"><p><span style=\"font-weight: normal;\"><font size=\"3\">各二级单位党组织：</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">2025年元旦春节将至，为深入贯彻落实中央八项规定精神，防止“四风”问题反弹回潮，杜绝腐败，现就有关廉洁纪律重申如下：</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">一、加强教育引导。各二级单位党组织要立足于学校当前重点工作，认真组织学习中共中央纪委印发的《关于做好2025年元旦春节期间正风肃纪工作的通知》精神，重点要求党员干部和行使公权力教职工将纪律规矩挺在前面，增强廉洁自律的自觉，筑牢不想腐的思想堤坝。</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">二、遵守“六个严禁”。要求全体教职员工务必做到六个严禁：1.严禁超标准、超范围公务接待、同城或异地接待、违规接受管理对象的宴请、不吃公款吃老板、“一桌餐”等各类违规吃喝问题；2.严禁用公款购买或违规收受有价证券和各种提货券、购物卡（含电子预付卡、电子礼券、微信红包等）、土特产；3.严禁公车私用、私车公养、违规使用公务加油卡，违规租用、借用下属单位或个人、服务对象车辆等问题；4.严禁借考察、学习培训、会议、调研等名义变相公款旅游或“借道”旅游，并将有关花费纳入公款报销等问题；5.严禁利用婚丧嫁娶、乔迁履新等大操大办和借机敛财；6.严禁超标准、超范围发放各类津贴补贴，违规自行设定名目、项目发放奖金补贴，违规以购物卡、现金等方式发放工会福利等问题。</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">三、规范公车使用。节日期间,&nbsp;有公务标识的车集中停放在后勤集团交通服务中心，救护车辆停放在医院，法人实体（法商学院、亿优公司等单位）的公车一律封存停放在行政楼前停车场。因工作需要用车的，应提前向纪委监专办综合室书面报备。为强化监督检查力度，省纪委监委运用公车信息化管理平台、公安交管“天眼”等系统，采取对重点区域明察暗访、对用车申请与用车情况进行比对、对公车集中停放点进行查看等方式，开展监督检查；校纪委监专办将按要求对公车使用情况开展随机抽查。</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">四、严格执纪问责。对节日期间发现的违反廉洁纪律问题，学校将及时予以公开曝光，并依照有关规定对当事人进行严肃处理。</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">学校办公室&nbsp;&nbsp;纪委监专办综合室</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\"> 2023年12月29日</font></span><br/></p><p><span style=\"font-weight: normal;\"><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></span></p><p><span style=\"font-weight: normal;\"><font size=\"3\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<br/></font></span></p><br/></h1>', 23, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/a3d41409-83c0-4358-802b-901d574c2bed/2.png', b'1', b'1', 4, 1);
INSERT INTO `t_announcement` VALUES (3, '五一放假公告', '<article><p>全校师生：</p><p>值五一劳动节来临之际，根据国家法定节假日规定，结合公司实际情况，现将2025年劳动节放假通知如下：</p><p>放假时间：</p><p>2025年5月1日-5月5日，共放假5天。4月28日（周日）、5月11日（周六）正常上课。</p><p>祝大家节日快乐，全力以赴，定会满载而归！</p><p>2025年4月28日</p></article>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/7217cf3d-a95d-4da4-a31b-f274dba408e3/3.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (4, '停电公告', '<p>校内各单位及用户：</p><p>为了完善学校配电室电力运行监控系统建设，后勤集团拟停电施工，具体停电时间及范围如下：</p><table><tbody><tr><td><p>序号</p></td><td><p>停电时间</p></td><td><p>施工地点</p></td><td><p>停电范围</p></td><td><p>备注</p></td></tr><tr><td><p>1</p></td><td><p>2025年4月30日晚 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;23:00-6：00</p></td><td><p>3号配电室 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8号配电室</p></td><td><p>行政楼、北大门、北二门、校医院、竹苑、北大门商业街</p></td><td><p>行政楼保密室回路暂不停电</p></td></tr><tr><td><p>2</p></td><td><p>2025年5月1日晚 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;23:00-6：00</p></td><td><p>11号配电室 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;12号配电室</p></td><td><p>教学楼4-5、体育馆、人文艺术楼、明辨楼、法商综合楼</p></td><td><p>&nbsp;</p></td></tr></tbody></table><p><br/></p><p>由此给您带来不便，敬请谅解。</p><p>特此通知。</p><p>后勤集团</p><p>2025年4月25日</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/8893f21d-6352-4b85-bdf8-0da276287d70/4.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (5, '讲座通知', '<p>各位老师：</p><p>很荣幸地通知您，我们将邀请杨氏太极拳传承人，为大家举办一次传统养生“站桩”的讲座。站桩是一种传统的中国养生方法，对于提高身体健康和精神集中力有着显著的效果。</p><p>时间：2025年5月9日（星期四）&nbsp;&nbsp;下午15:30—17:00</p><p>地点：图书馆</p><p>主讲人：传统杨氏太极拳传承人</p><p>在这次讲座中，我们将讨论以下主题：</p><p>1、站桩的历史和起源</p><p>2、站桩的基本技巧和步骤</p><p>3、站桩对身心健康的好处</p><p>4、如何在日常生活中实践站桩</p><p>5、站桩的常见问题和解答</p><p>我们诚挚邀请您参加这次讲座，无论您是站桩的初学者，还是已经有一定经验的爱好者，都能从中获得有价值的信息和启发。</p><p>&nbsp; &nbsp; &nbsp; 期待在讲座中与您相见！</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;校工会</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2025年5月6日</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/a3b4f961-73b4-441e-9177-4b755926a23a/3.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (6, '宣传部：2025年5月政治理论学习安排', '<p>各二级单位党委（党总支、直属党支部）：</p><p>根据《党委理论学习中心组学习实施办法》有关规定以及上级部门有关理论学习要求，现就2025年5月理论学习安排如下：</p><p><br/></p><p><strong>一、学习时间</strong></p><p>2025年5月（周四下午）</p><p><br/></p><p><strong>二、学习内容</strong></p><p>1.《中国共产党纪律处分条例》；</p><p>2.习近平总书记在4月30日中共中央政治局会议上的重要讲话精神；</p><p>3.习近平总书记五四青年节对全国广大青年寄语精神;</p><p>4.习近平总书记“五一”国际劳动节前夕的重要指示精神；</p><p>5.习近平总书记在主持召开新时代推动西部大开发座谈会时的重要讲话精神；</p><p>6.习近平总书记在重庆考察时的重要讲话精神；</p><p>7.习近平总书记视察陆军军医大学时的重要讲话精神；</p><p>8.习近平：组织动员亿万职工积极投身强国建设、民族复兴的伟大事业（《求是》2025年第9期）；</p><p>9.习近平：加强文化遗产保护传承 弘扬中华优秀传统文化（《求是》2025年第8期）。&nbsp;</p><p><br/></p><p><strong>三、学习形式</strong></p><p>集体学习研讨、个人自学与专题调研相结合。</p><p><br/></p><p><strong>四、学习要求</strong></p><p>请各二级党组织认真做好本单位的5月理论学习安排，党委理论学习中心组要发挥示范带头作用，以适当形式选学上述内容，并切实组织好教职工理论学习。</p><p><br/></p><p>附件：</p><p>1.《中国共产党纪律处分条例》</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=9401801122404623559&amp;item_id=9401801122404623559\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=9401801122404623559&amp;item_id=9401801122404623559</a></p><p>2.习近平总书记在4月30日中共中央政治局会议上的重要讲话精神</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=15798324532441118299&amp;item_id=15798324532441118299\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=15798324532441118299&amp;item_id=15798324532441118299</a></p><p>3.习近平总书记五四青年节对全国广大青年寄语精神</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=10730859303566923963&amp;item_id=10730859303566923963\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=10730859303566923963&amp;item_id=10730859303566923963</a></p><p>4.习近平总书记“五一”国际劳动节前夕的重要指示精神</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=11292706342525683699&amp;item_id=11292706342525683699\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=11292706342525683699&amp;item_id=11292706342525683699</a></p><p>5.习近平总书记在主持召开新时代推动西部大开发座谈会时的重要讲话精神</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=14183980288229771544&amp;item_id=14183980288229771544\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=14183980288229771544&amp;item_id=14183980288229771544</a></p><p>6.习近平总书记在重庆考察时的重要讲话精神</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=18337751892991741803&amp;item_id=18337751892991741803\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=18337751892991741803&amp;item_id=18337751892991741803</a></p><p>7.习近平总书记视察陆军军医大学时的重要讲话精神</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=14068944580733469805&amp;item_id=14068944580733469805\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=14068944580733469805&amp;item_id=14068944580733469805</a></p><p>8.习近平：组织动员亿万职工积极投身强国建设、民族复兴的伟大事业（《求是》2025年第9期）</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=11410017133327814710&amp;item_id=11410017133327814710\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=11410017133327814710&amp;item_id=11410017133327814710</a></p><p>9.习近平：加强文化遗产保护传承 弘扬中华优秀传统文化（《求是》2025年第8期）</p><p><a href=\"https://www.xuexi.cn/lgpage/detail/index.html?id=14077892756282163430&amp;item_id=14077892756282163430\" target=\"_self\">https://www.xuexi.cn/lgpage/detail/index.html?id=14077892756282163430&amp;item_id=14077892756282163430</a></p><p><br/></p><p>党委宣传部</p><p>2025年5月6日</p><p><br/></p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/f1ba795b-a248-4d97-9ec7-79ed7441ede0/5.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (7, '资产处：关于开展资产管理绩效评价工作的通知', '<p>各单位：</p><p>根据省财政厅要求、学校国有资产管理相关规定和工作安排，现开展全校二级单位2023年度资产管理绩效评价，具体事宜通知如下：</p><p>一、考评范围</p><p>各二级单位（各独立核算单位另自主组织）</p><p>二、工作安排</p><p>资产管理绩效评价工作的组织实施分为自查自评、边查边改、综合评定三个阶段。</p><p>（一）自查自评（4月29日—5月15日）</p><p>各二级单位结合2023年度资产清查结果、资产管理系统信息变更登记进行清理，对本单位在账资产进行自查，主要核查资产管理系统数据是否全面准确，单位和人员信息对照是否存在遗漏和误差，资产使用人和存放地填写是否规范准确，是否存在资产数据信息不准确、不完整等问题，并及时在资产管理系统中进行更正、补充。</p><p>（二）边查边改（4月30日—5月20日）</p><p>各二级单位结合自查情况，对照《湖北省行政事业单位国有资产监督管理条例》《湖北经济学院国有资产管理办法》《湖北经济学院通用办公设备及家具配置管理实施细则》《湖北经济学院国有资产处置管理实施细则》等制度要求，客观地就本单位2023年度资产管理绩效进行自评，并填写《湖北经济学院资产管理绩效综合评价表》（见附件）。</p><p>（三）综合评定（5月31日前）</p><p>学校资产部门结合各单位自评及改进情况，通过系统台账查询或现场核查等方式，对各部门资产管理情况进行综合评定，填写《湖北经济学院资产管理绩效综合评价表》。</p><p>三、有关要求</p><p>各单位主要负责人、资产管理员须按上级单位要求完成好学校布置的2023年度资产管理绩效评价工作。在自查及改进过程，各部门资产管理员须认真核对账面资产信息，对查找出的有关情形立查立改，暂无法自行解决的，要及时联系资产处资产科协调，确保数据信息准确无误，固定资产未粘贴标签等情况要进行补打及粘贴等处理。</p><p>各单位资产管理绩效评价结果与资产配置、资源配给和年度目标责任考核挂钩。</p><p>请各单位于5月20日16:00前，将《湖北经济学院资产管理绩效综合评价表》电子版和纸质版（部门负责人签字加盖公章）交资产处资产科。</p><p>&nbsp;&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 资产与设备管理处&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 2025年4月28日&nbsp;</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/cb4b56e3-f4c1-4db7-868c-4d529b8f821a/4.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (8, '关于举办第二十一届校体育运动会的通知', '<p>校内各单位：</p><p>&nbsp; &nbsp; &nbsp; &nbsp;为深入贯彻中共中央办公厅、国务院办公厅印发的《关于全面加强和改进新时代学校体育工作的意见》《关于深化体教融合促进青少年健康发展的意见文件精神》等文件精神，落实《湖北经济学院关于加强和改进新时代体育工作的实施方案》，提升我校青年学生的体质健康水平和运动技术水平，促进学生全面发展，丰富校园体育文化氛围，学校决定举办第二十一届体育运动会。现将有关事项通知如下：&nbsp;&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp;一、本届体育运动会是一次综合性的运动会，为适应学校群体活动新的发展需求，扩大学生参与体育活动的积极性，本届体育运动会设置田径、龙舟、足球、篮球、排球、羽毛球、乒乓球等竞赛项目，各单项竞赛规程另行通知，比赛时间从2025年3月至11月。&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp;二、各学院要以体育运动会为契机，大力开展体育运动，弘扬体育精神，并切实按照《第二十一届体育运动会总则》和各项目竞赛规程的要求，做好宣传、动员、组队、报名、训练、参赛等各项工作。在各项比赛期间，各学院要加强学生安全教育与管理，认真组织学生参加和观看比赛，并确保学生人身与财产安全。</p><p>&nbsp; &nbsp; &nbsp; &nbsp;三、本届运动会比赛项目多，时间跨度大，组织难度高，各专门工作组和相关单位要认真研究制定竞赛、宣传、后勤保障及安全保卫等工作方案，精心组织，努力工作，确保本届体育运动会取得圆满成功。</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;学院体育运动委员会</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;2025年3月20日</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/4b321c34-1102-43af-be3e-d8a8854b822a/2.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (9, '关于开展“大美学院、人人共建”植树活动的通知', '<p>校内各单位：</p><p>为进一步增强师生的爱校情感，加强校园人文生态环境建设，学校决定于3月22日（周五）下午举行“大美经院、人人共建”植树活动。现将具体安排通知如下：</p><p><strong>一、活动时间：</strong>2025年3月22日（周五）17:00</p><p><strong>二、活动地点：</strong>经世路</p><p><strong>三、参加人员</strong></p><p>1.全体校领导；</p><p>2.在校学生代表；</p><p>3.各学院、职能部门的教职工代表，校友代表。</p><p><strong>四、注意事项</strong></p><p>1.学生工作处负责号召和有序组织在校学生参与本次植树活动，并安排优秀学生发言。</p><p>2.合作发展处负责联系校友，邀请校友回校参与植树活动。</p><p>3.请所有参与植树活动的人员提前15分钟到达活动场地,植树过程中听从工作人员的安排，务必注意安全。</p><p>&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 绿化管理工作领导小组</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;2025年3月15日</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/05/06/23d87f2b-1636-4a98-8710-fe5ca83398cb/1.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (10, '关于做好2025年度学校规章制度“立改废”工作的通知', '<p>校内各单位：</p><p>为推进依法治校，完善学校制度体系，按照《规范性文件管理办法》，现就2025年度学校规范性文件“立改废”工作有关事项通知如下：</p><p><strong>一、全面清理制度</strong></p><p>各单位认真梳理现行规章制度（含“试行”“暂行”满1年的规章制度），根据实际工作需要，拟定2025年规范性文件制定、修订或废止计划（附件）。</p><p>学校现行制度请参考《制度汇编（2002年9月-2025年1月）》，见OA系统-文档中心-规章制度。</p><p><strong>二、时间安排</strong></p><p><strong>1．3月8日前，</strong>各单位填写2025年规范性文件制定、修订或废止计划表，报请分管校领导签字同意后，纸质版报学校办公室（行政楼A313）。无制度制定、修订或废止计划的单位不需提交表格。</p><p><strong>2．3月8日-4月初，</strong>学校办公室汇总、审核全校计划，提交校党委常委会审定后发布。</p><p><strong>3．4月初-11月30日前，</strong>各单位根据学校计划时限要求，开展起草、修订工作，按照规定程序完成送审稿，报学校办公室分类办理，原则上11月30日前完成全年计划制度的审核发布。</p><p><strong>三、工作要求</strong></p><p><strong>1．规范工作程序。</strong>文件起草、修订必须充分酝酿、调研和论证，在一定范围内征求意见，提交相关单位会签并完成规范性审查。需上会审定的制度分管校领导应组织召开专题会议讨论。制度送审稿至少提前2周报学校办公室，以便及时安排上会。</p><p><strong>2．加强审核把关。</strong>学校办公室负责审核各项制度提交材料，起草说明需包含起草必要性、主要依据、主要内容、有关方面意见及采纳情况等，要素不充分、不完整的不予受理，未经过分管校领导把关、未记录专题会议讨论情况的不予上会审议。</p><p><strong>3．实行分类办理。</strong>对不涉及制度篇目结构修改、管理权限调整等实质性条款修订，个别条款不涉及原则性问题或其内容已经集体讨论的，分管校领导审定后重新发文颁行；新制定和进行重大修订的制度按程序提交校党委常委会、校长办公会审定后印发实施。</p><p><br/></p><p>学校办公室&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p><p>2025年2月28日&nbsp;</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/2d1b0b9e-01e3-4fd8-872f-bdc5b4ce0dbf/4.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (11, '9', '<p>9</p>', 1, '2025-10-21 16:01:14', b'1', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/29/aeeb0af0-4568-4e2f-8bdf-3d8616bd28a0/4.png', b'0', b'0', 4, 1);
INSERT INTO `t_announcement` VALUES (12, '1', '<p>1</p>', 1, '2025-10-21 16:01:14', b'1', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/05/07/e4836556-7126-4a78-b25c-00b4b14649e8/4.png', b'0', b'0', 3, 1);
INSERT INTO `t_announcement` VALUES (13, '111', '<p>22</p>', 1, '2025-10-21 16:01:14', b'1', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/05/10/fc3a77e0-a642-4801-bf69-692d4d461b61/4.png', b'0', b'0', 1, 1);
INSERT INTO `t_announcement` VALUES (14, '222', '<p>2</p>', 1, '2025-10-21 16:01:14', b'1', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/05/10/0f8ff5f5-cb1e-49b2-bcc4-ae46379a883e/4.png', b'0', b'0', NULL, 1);
INSERT INTO `t_announcement` VALUES (15, '通知信息10', '<p>通知信息</p>', 1, '2025-10-21 16:01:14', b'1', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/05/10/77f3dd8a-c2fe-4d1e-afd1-3d8d61aa6550/4.png', b'0', b'0', 1, 1);
INSERT INTO `t_announcement` VALUES (16, '1', '<p>1</p>', 1, '2025-10-21 16:01:14', b'1', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/05/10/a7a5cec9-ee9e-4a76-97e8-2d52ba42a33d/4.png', b'0', b'0', 1, 1);
INSERT INTO `t_announcement` VALUES (17, '欢迎郑州工程技术学院接入智训平台', '<p>尊敬的郑州工程技术学院同学们：</p><p>大家好！首先，请允许我们代表智训平台向你们致以最热烈的欢迎！在这个充满机遇与挑战的时代，我们深知学习的重要性不仅在于获取知识，更在于如何高效地吸收、运用这些知识。因此，为了帮助大家更好地适应现代学习方式，提高个人竞争力，我们特别推出了智训平台。</p><p>在这里，每一位同学都可以找到适合自己的学习资源与工具。无论是专业课程的学习资料，还是跨学科学习的兴趣探索，亦或是职业规划与发展建议，智训平台都能提供全面的支持和服务。我们的目标是打造一个开放、共享、互动的学习社区，让每位用户都能在轻松愉快的氛围中成长进步。</p><p>此外，智训平台还具有以下特色功能：</p><ul><li><strong>个性化学习路径推荐</strong>：根据每位同学的专业背景和个人兴趣，智能推荐最适合的学习路径。</li><li><strong>互动交流区</strong>：设立专门的讨论板块，鼓励同学们分享学习心得，互相解答疑惑，共同进步。</li><li><strong>实时反馈系统</strong>：通过在线测试和练习题，及时了解自己的学习效果，并获得针对性的改进建议。</li><li><strong>职业发展指导</strong>：提供行业动态、求职技巧等信息，助力同学们规划未来职业生涯。</li></ul><p>我们相信，在智训平台的帮助下，每一位西安美术学院的同学都能够更加自信地面对未来的挑战。让我们携手并进，开启一段精彩纷呈的学习旅程吧！</p><p>最后，如果在使用过程中有任何问题或建议，欢迎随时联系我们。智训平台将始终致力于为用户提供最佳的学习体验。</p><p>祝学习愉快！</p><p><br/></p><p>智训平台团队\n2025年11月11日</p>', 1, '2025-10-21 16:01:14', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/11/11/96cb588e-7f03-4d85-8daa-82caad240e36/a.jpg', b'1', b'1', 1, 1);
INSERT INTO `t_announcement` VALUES (18, '软件工程第一次月考', '<p>各位同学：</p><p>大家好！为检验大家对软件工程课程前一阶段知识的掌握情况，现组织第一次月考</p>', 1, '2025-04-11 22:54:39', b'0', 'http://uejyptad.ueweixin.com/ueupload//announcement/2025/04/11/d2a03236-a69c-4649-b793-6a0556fbc5e5/1.png', b'1', b'1', 3, 1);

-- ----------------------------
-- Table structure for t_announcement_archive
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement_archive`;
CREATE TABLE `t_announcement_archive`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `parent_id` int NULL DEFAULT NULL,
  `level` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `item_order` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_announcement_archive
-- ----------------------------
INSERT INTO `t_announcement_archive` VALUES (1, '通知公告', 1, '2023-12-14 08:43:46', b'0', NULL, '/通知公告/', 1, 10);
INSERT INTO `t_announcement_archive` VALUES (2, '考试安排', 1, '2023-12-14 08:43:59', b'1', 1, '/通知公告/考试安排/', 1, 20);
INSERT INTO `t_announcement_archive` VALUES (3, '考试安排', 1, '2023-12-14 08:44:06', b'0', NULL, '/考试安排/', 1, 30);
INSERT INTO `t_announcement_archive` VALUES (4, '测试公告', 1, '2025-04-29 11:23:19', b'1', NULL, '/测试公告/', 1, 40);
INSERT INTO `t_announcement_archive` VALUES (5, '1', 1, '2025-04-29 11:23:22', b'1', 4, '/测试公告/1/', 1, 50);
INSERT INTO `t_announcement_archive` VALUES (6, '2·', 1, '2025-04-29 11:23:25', b'1', 5, '/测试公告/1/2·/', 1, 60);
INSERT INTO `t_announcement_archive` VALUES (7, '1', 1, '2025-04-29 11:31:49', b'1', 5, '/测试公告/1/1/', 1, 70);
INSERT INTO `t_announcement_archive` VALUES (8, '测试公告', 1, '2025-05-07 16:50:56', b'1', 4, '/测试公告/测试公告/', 1, 80);

-- ----------------------------
-- Table structure for t_announcement_department
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement_department`;
CREATE TABLE `t_announcement_department`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `announcement_id` int NULL DEFAULT NULL,
  `department_id` int NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `create_user_id` int NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 280 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_announcement_department
-- ----------------------------
INSERT INTO `t_announcement_department` VALUES (1, 1, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (2, 1, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (3, 1, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (4, 1, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (5, 1, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (6, 1, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (7, 1, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (8, 2, 1, b'1', 23, 1);
INSERT INTO `t_announcement_department` VALUES (9, 2, 2, b'1', 23, 1);
INSERT INTO `t_announcement_department` VALUES (10, 2, 8, b'1', 23, 1);
INSERT INTO `t_announcement_department` VALUES (11, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (12, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (13, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (14, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (15, 3, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (16, 4, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (17, 4, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (18, 5, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (19, 6, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (20, 7, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (21, 8, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (22, 9, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (23, 10, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (24, 11, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (25, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (26, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (27, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (28, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (29, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (30, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (31, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (32, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (33, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (34, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (35, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (36, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (37, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (38, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (39, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (40, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (41, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (42, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (43, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (44, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (45, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (46, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (47, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (48, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (49, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (50, 3, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (51, 3, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (52, 4, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (53, 4, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (54, 5, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (55, 6, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (56, 7, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (57, 8, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (58, 9, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (59, 10, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (60, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (61, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (62, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (63, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (64, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (65, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (66, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (67, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (68, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (69, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (70, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (71, 8, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (72, 9, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (73, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (74, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (75, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (76, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (77, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (78, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (79, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (80, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (81, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (82, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (83, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (84, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (85, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (86, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (87, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (88, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (89, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (90, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (91, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (92, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (93, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (94, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (95, 10, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (96, 10, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (97, 10, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (98, 10, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (99, 10, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (100, 10, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (101, 10, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (102, 10, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (103, 10, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (104, 10, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (105, 10, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (106, 9, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (107, 9, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (108, 9, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (109, 9, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (110, 9, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (111, 9, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (112, 9, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (113, 9, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (114, 9, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (115, 9, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (116, 9, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (117, 9, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (118, 9, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (119, 9, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (120, 9, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (121, 9, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (122, 9, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (123, 9, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (124, 9, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (125, 9, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (126, 9, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (127, 9, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (128, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (129, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (130, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (131, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (132, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (133, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (134, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (135, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (136, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (137, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (138, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (139, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (140, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (141, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (142, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (143, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (144, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (145, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (146, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (147, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (148, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (149, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (150, 8, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (151, 8, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (152, 9, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (153, 9, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (154, 9, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (155, 9, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (156, 9, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (157, 9, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (158, 9, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (159, 9, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (160, 9, 10, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (161, 9, 11, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (162, 9, 14, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (163, 3, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (164, 3, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (165, 3, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (166, 3, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (167, 3, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (168, 3, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (169, 12, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (170, 12, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (171, 12, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (172, 12, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (173, 12, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (174, 12, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (175, 2, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (176, 2, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (177, 2, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (178, 2, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (179, 2, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (180, 2, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (181, 2, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (182, 2, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (183, 2, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (184, 2, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (185, 2, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (186, 2, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (187, 2, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (188, 2, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (189, 2, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (190, 2, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (191, 2, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (192, 2, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (193, 2, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (194, 2, 10, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (195, 2, 11, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (196, 2, 14, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (197, 1, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (198, 1, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (199, 1, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (200, 1, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (201, 1, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (202, 10, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (203, 10, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (204, 10, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (205, 10, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (206, 10, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (207, 10, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (208, 10, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (209, 10, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (210, 10, 10, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (211, 10, 11, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (212, 10, 14, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (213, 13, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (214, 13, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (215, 13, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (216, 13, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (217, 13, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (218, 13, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (219, 13, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (220, 13, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (221, 13, 10, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (222, 13, 11, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (223, 13, 14, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (224, 14, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (225, 14, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (226, 14, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (227, 14, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (228, 14, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (229, 14, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (230, 14, 10, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (231, 14, 11, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (232, 14, 14, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (233, 14, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (234, 14, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (235, 14, 1, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (236, 14, 2, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (237, 14, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (238, 14, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (239, 15, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (240, 15, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (241, 15, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (242, 15, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (243, 15, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (244, 15, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (245, 15, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (246, 15, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (247, 15, 10, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (248, 15, 11, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (249, 15, 14, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (250, 16, 1, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (251, 16, 2, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (252, 8, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (253, 17, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (254, 17, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (255, 17, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (256, 17, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (257, 17, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (258, 17, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (259, 17, 3, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (260, 17, 5, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (261, 17, 6, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (262, 17, 7, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (263, 17, 8, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (264, 17, 9, b'1', 1, 1);
INSERT INTO `t_announcement_department` VALUES (265, 18, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (266, 18, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (267, 18, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (268, 18, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (269, 18, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (270, 18, 9, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (271, 18, 10, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (272, 18, 11, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (273, 18, 14, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (274, 17, 3, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (275, 17, 5, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (276, 17, 6, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (277, 17, 7, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (278, 17, 8, b'0', 1, 1);
INSERT INTO `t_announcement_department` VALUES (279, 17, 9, b'0', 1, 1);

-- ----------------------------
-- Table structure for t_announcement_read
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement_read`;
CREATE TABLE `t_announcement_read`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `announcement_id` int NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_announcement_read
-- ----------------------------
INSERT INTO `t_announcement_read` VALUES (28, 8, 2, '2025-04-11 17:52:00', 6);
INSERT INTO `t_announcement_read` VALUES (29, 9, 2, '2025-04-11 17:52:09', 6);
INSERT INTO `t_announcement_read` VALUES (30, 1, 2, '2025-04-11 17:53:40', 6);
INSERT INTO `t_announcement_read` VALUES (31, 18, 2, '2025-04-12 17:02:57', 6);
INSERT INTO `t_announcement_read` VALUES (32, 17, 2, '2025-04-12 17:03:03', 6);
INSERT INTO `t_announcement_read` VALUES (33, 2, 2, '2025-04-12 17:03:10', 6);
INSERT INTO `t_announcement_read` VALUES (34, 10, 2, '2025-04-12 17:03:53', 6);
INSERT INTO `t_announcement_read` VALUES (35, 4, 2, '2025-04-25 22:20:51', 6);

-- ----------------------------
-- Table structure for t_apply
-- ----------------------------
DROP TABLE IF EXISTS `t_apply`;
CREATE TABLE `t_apply`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报名名称',
  `limit_end_time` datetime NULL DEFAULT NULL COMMENT '考试结束时间',
  `limit_start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  `limited` bit(1) NULL DEFAULT NULL COMMENT '是否限制人数',
  `count` int NULL DEFAULT NULL COMMENT '限制报名人数量',
  `status` int NULL DEFAULT NULL COMMENT '报名状态：1.未发布  2.已发布  3.已关闭',
  `apply_end_time` datetime NULL DEFAULT NULL COMMENT '报名截止时间',
  `already_apply_count` int NULL DEFAULT NULL COMMENT '已报名人数',
  `apply_archive_id` int NULL DEFAULT NULL COMMENT '报名分类',
  `create_department_id` int NULL DEFAULT NULL COMMENT '创建者部门id',
  `need_audit` bit(1) NULL DEFAULT NULL COMMENT '是否需要审核',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_apply
-- ----------------------------
INSERT INTO `t_apply` VALUES (1, 'Shell编程课程-考试报名', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 50, 3, '2025-11-18 00:00:00', 2, 1, 1, b'1');
INSERT INTO `t_apply` VALUES (2, '考试报名1期（Shell编程课程）', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 1, 3, '2025-11-18 00:00:00', 0, 2, 1, b'1');
INSERT INTO `t_apply` VALUES (3, '考试报名2期（Shell编程课程）', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 1, 3, '2025-11-18 00:00:00', 1, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (4, 'Shell编程考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'1', b'1', 50, 3, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (5, 'Shell编程考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 50, 2, '2025-11-18 00:00:00', 1, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (6, 'Shell编程考试-第2次', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 100, 1, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (7, 'Shell编程考试-第3次', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 50, 1, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (8, 'Shell编程-考试报名', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 50, 2, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (9, '银河麒麟操作系统基础课程-1期考试报名', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 30, 2, '2025-11-18 00:00:00', 0, 2, 1, b'0');
INSERT INTO `t_apply` VALUES (10, '软件工程第一次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (11, '软件工程第二次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (12, '软件工程第三次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'1', b'0', NULL, 1, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (13, '网络工程第2次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (14, '网络工程第1次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (15, '软件工程第1次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 2, 1, b'0');
INSERT INTO `t_apply` VALUES (16, '网络安全第1次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (17, '网络安全第2次考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (18, '物联网工程第1次测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 3, '2025-11-18 00:00:00', 0, 2, 1, b'0');
INSERT INTO `t_apply` VALUES (19, '物联网工程第2次测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 2, 1, b'0');
INSERT INTO `t_apply` VALUES (20, '物联网工程第3次测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (21, '物联网工程第4次测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (22, '物联网工程第5次测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (23, '物联网工程第6次测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (24, '报名软件测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (25, '测试报名11111', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'1', 50, 2, '2025-11-18 00:00:00', 1, 4, 1, b'1');
INSERT INTO `t_apply` VALUES (26, '1', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'1', b'0', NULL, 1, '2025-11-18 00:00:00', 0, NULL, 1, b'0');
INSERT INTO `t_apply` VALUES (27, '第一次测试报名', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (28, '软件工程拔高测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 1, 1, b'1');
INSERT INTO `t_apply` VALUES (29, 'RAG理论测试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (30, '计算机', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 1, 1, b'1');
INSERT INTO `t_apply` VALUES (31, '计算机网络基础一期', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 2, 1, b'1');
INSERT INTO `t_apply` VALUES (32, '计算机网络基础课程', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 2, 1, b'1');
INSERT INTO `t_apply` VALUES (33, '计算机网络', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 2, 1, b'1');
INSERT INTO `t_apply` VALUES (34, '计算机基础课程', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 1, 1, b'1');
INSERT INTO `t_apply` VALUES (35, 'AIGC美术结业考试', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 1, 1, b'0');
INSERT INTO `t_apply` VALUES (36, '测试123', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'1', b'0', NULL, 3, '2025-11-18 00:00:00', 0, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (37, '考试报名张三', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'1', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (38, '考试报名张三', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 1, 4, 1, b'0');
INSERT INTO `t_apply` VALUES (39, '考试报名', '2025-11-21 00:00:00', '2025-11-19 00:00:00', 1, '2025-11-14 20:28:53', b'0', b'0', NULL, 2, '2025-11-18 00:00:00', 0, 3, 1, b'0');
INSERT INTO `t_apply` VALUES (40, '2025年国际大学生程序设计竞赛（ICPC）预算赛', '2025-06-17 00:00:00', '2025-06-12 00:00:00', 1, '2025-04-29 08:25:16', b'0', b'0', NULL, 2, '2025-05-30 00:00:00', 0, 1, 1, b'0');

-- ----------------------------
-- Table structure for t_apply_archive
-- ----------------------------
DROP TABLE IF EXISTS `t_apply_archive`;
CREATE TABLE `t_apply_archive`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `parent_id` int NULL DEFAULT NULL,
  `level` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `item_order` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_apply_archive
-- ----------------------------
INSERT INTO `t_apply_archive` VALUES (1, '考试报名', 1, '2023-12-14 15:55:57', b'0', NULL, '/考试报名/', 1, 10);
INSERT INTO `t_apply_archive` VALUES (2, '报名1期', 1, '2023-12-14 15:56:11', b'0', NULL, '/报名1期/', 1, 20);
INSERT INTO `t_apply_archive` VALUES (3, '报名2期', 1, '2023-12-14 15:56:16', b'0', NULL, '/报名2期/', 1, 30);
INSERT INTO `t_apply_archive` VALUES (4, '测试报名', 1, '2025-04-29 11:26:50', b'0', NULL, '/测试报名/', 1, 40);
INSERT INTO `t_apply_archive` VALUES (5, '1', 1, '2025-04-29 11:27:03', b'1', 4, '/测试报名/1/', 1, 50);
INSERT INTO `t_apply_archive` VALUES (6, '2', 1, '2025-04-29 11:27:05', b'1', 5, '/测试报名/1/2/', 1, 60);
INSERT INTO `t_apply_archive` VALUES (7, '3', 1, '2025-04-29 11:27:07', b'1', 6, '/测试报名/1/2/3/', 1, 70);
INSERT INTO `t_apply_archive` VALUES (8, '3', 1, '2025-04-29 11:27:11', b'1', 7, '/测试报名/1/2/3/3/', 1, 80);
INSERT INTO `t_apply_archive` VALUES (9, '3', 1, '2025-04-29 11:27:15', b'1', 8, '/测试报名/1/2/3/3/3/', 1, 90);
INSERT INTO `t_apply_archive` VALUES (10, '1', 1, '2025-04-29 11:27:26', b'1', NULL, '/1/', 1, 100);
INSERT INTO `t_apply_archive` VALUES (11, '2', 1, '2025-04-29 11:27:57', b'1', NULL, '/2/', 1, 110);
INSERT INTO `t_apply_archive` VALUES (12, '2', 1, '2025-04-29 11:28:00', b'1', 11, '/2/2/', 1, 120);
INSERT INTO `t_apply_archive` VALUES (13, '2', 1, '2025-04-29 11:28:16', b'1', 12, '/2/2/2/', 1, 130);

-- ----------------------------
-- Table structure for t_apply_audit
-- ----------------------------
DROP TABLE IF EXISTS `t_apply_audit`;
CREATE TABLE `t_apply_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_id` int NULL DEFAULT NULL COMMENT '报名id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_department_id` int NULL DEFAULT NULL COMMENT '创建人部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_apply_audit
-- ----------------------------
INSERT INTO `t_apply_audit` VALUES (1, 1, 4, b'1', '2023-12-14 15:58:47', 7);
INSERT INTO `t_apply_audit` VALUES (2, 1, 2, b'1', '2023-12-14 16:03:21', 6);
INSERT INTO `t_apply_audit` VALUES (3, 2, 4, b'1', '2023-12-14 16:14:11', 7);
INSERT INTO `t_apply_audit` VALUES (4, 2, 2, b'1', '2023-12-14 16:14:27', 6);
INSERT INTO `t_apply_audit` VALUES (5, 25, 9, b'1', '2025-05-14 10:37:33', 11);
INSERT INTO `t_apply_audit` VALUES (6, 25, 9, b'1', '2025-05-14 10:38:02', 11);
INSERT INTO `t_apply_audit` VALUES (7, 28, 30, b'1', '2025-08-22 09:52:32', 6);
INSERT INTO `t_apply_audit` VALUES (8, 30, 2, b'1', '2025-10-22 14:57:51', 6);
INSERT INTO `t_apply_audit` VALUES (9, 31, 2, b'1', '2025-10-22 15:12:55', 6);
INSERT INTO `t_apply_audit` VALUES (10, 32, 2, b'0', '2025-10-22 16:00:24', 6);
INSERT INTO `t_apply_audit` VALUES (11, 33, 2, b'1', '2025-10-22 17:20:14', 6);

-- ----------------------------
-- Table structure for t_apply_department
-- ----------------------------
DROP TABLE IF EXISTS `t_apply_department`;
CREATE TABLE `t_apply_department`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `apply_id` int NULL DEFAULT NULL,
  `department_id` int NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `create_user_id` int NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 275 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_apply_department
-- ----------------------------
INSERT INTO `t_apply_department` VALUES (1, 1, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (2, 1, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (3, 1, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (4, 1, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (5, 1, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (6, 2, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (7, 2, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (8, 2, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (9, 2, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (10, 2, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (11, 3, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (12, 3, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (13, 3, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (14, 3, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (15, 3, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (16, 4, 3, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (17, 4, 5, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (18, 4, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (19, 4, 7, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (20, 4, 8, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (21, 4, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (22, 4, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (23, 4, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (24, 4, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (25, 4, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (26, 5, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (27, 5, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (28, 5, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (29, 5, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (30, 5, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (31, 6, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (32, 6, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (33, 6, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (34, 6, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (35, 6, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (36, 7, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (37, 8, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (38, 8, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (39, 8, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (40, 8, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (41, 8, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (42, 8, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (43, 9, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (44, 9, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (45, 9, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (46, 9, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (47, 9, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (48, 9, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (49, 10, 1, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (50, 10, 2, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (51, 10, 3, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (52, 10, 5, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (53, 10, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (54, 10, 7, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (55, 10, 8, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (56, 10, 9, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (57, 10, 10, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (58, 10, 11, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (59, 10, 14, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (60, 10, 1, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (61, 10, 2, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (62, 10, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (63, 10, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (64, 10, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (65, 10, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (66, 10, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (67, 10, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (68, 10, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (69, 10, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (70, 10, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (71, 11, 1, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (72, 11, 2, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (73, 11, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (74, 11, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (75, 11, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (76, 11, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (77, 11, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (78, 11, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (79, 11, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (80, 11, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (81, 11, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (82, 12, 1, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (83, 12, 2, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (84, 12, 3, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (85, 12, 5, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (86, 12, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (87, 12, 7, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (88, 12, 8, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (89, 12, 9, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (90, 12, 10, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (91, 12, 11, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (92, 12, 14, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (93, 12, 1, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (94, 12, 2, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (95, 12, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (96, 12, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (97, 12, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (98, 12, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (99, 12, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (100, 12, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (101, 12, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (102, 12, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (103, 12, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (104, 13, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (105, 13, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (106, 13, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (107, 13, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (108, 13, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (109, 13, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (110, 14, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (111, 14, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (112, 14, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (113, 14, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (114, 14, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (115, 14, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (116, 15, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (117, 15, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (118, 15, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (119, 15, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (120, 15, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (121, 15, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (122, 16, 3, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (123, 16, 5, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (124, 16, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (125, 16, 7, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (126, 16, 8, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (127, 16, 9, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (128, 16, 10, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (129, 16, 11, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (130, 16, 14, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (131, 17, 3, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (132, 17, 5, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (133, 17, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (134, 17, 7, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (135, 17, 8, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (136, 17, 9, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (137, 18, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (138, 18, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (139, 18, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (140, 18, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (141, 18, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (142, 18, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (143, 17, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (144, 17, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (145, 17, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (146, 17, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (147, 17, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (148, 17, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (149, 16, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (150, 16, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (151, 16, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (152, 16, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (153, 16, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (154, 16, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (155, 16, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (156, 16, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (157, 16, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (158, 19, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (159, 19, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (160, 19, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (161, 19, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (162, 19, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (163, 19, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (164, 20, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (165, 20, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (166, 20, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (167, 20, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (168, 20, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (169, 20, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (170, 21, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (171, 21, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (172, 21, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (173, 21, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (174, 21, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (175, 21, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (176, 22, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (177, 22, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (178, 22, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (179, 22, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (180, 22, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (181, 22, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (182, 23, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (183, 23, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (184, 23, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (185, 23, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (186, 23, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (187, 23, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (188, 24, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (189, 24, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (190, 24, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (191, 24, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (192, 24, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (193, 24, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (194, 24, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (195, 24, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (196, 24, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (197, 25, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (198, 25, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (199, 25, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (200, 25, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (201, 25, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (202, 25, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (203, 25, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (204, 25, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (205, 25, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (206, 26, 1, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (207, 26, 2, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (208, 27, 1, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (209, 27, 2, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (210, 27, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (211, 27, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (212, 27, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (213, 27, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (214, 27, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (215, 27, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (216, 27, 10, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (217, 27, 11, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (218, 27, 14, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (219, 28, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (220, 28, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (221, 28, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (222, 28, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (223, 28, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (224, 28, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (225, 29, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (226, 29, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (227, 30, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (228, 30, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (229, 31, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (230, 32, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (231, 33, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (232, 34, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (233, 35, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (234, 35, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (235, 35, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (236, 35, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (237, 35, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (238, 35, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (239, 36, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (240, 36, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (241, 36, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (242, 36, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (243, 36, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (244, 36, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (245, 37, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (246, 37, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (247, 37, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (248, 37, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (249, 37, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (250, 37, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (251, 38, 3, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (252, 38, 5, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (253, 38, 6, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (254, 38, 7, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (255, 38, 8, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (256, 38, 9, b'1', 1, 1);
INSERT INTO `t_apply_department` VALUES (257, 38, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (258, 38, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (259, 38, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (260, 38, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (261, 38, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (262, 38, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (263, 39, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (264, 39, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (265, 39, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (266, 39, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (267, 39, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (268, 39, 9, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (269, 40, 3, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (270, 40, 5, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (271, 40, 6, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (272, 40, 7, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (273, 40, 8, b'0', 1, 1);
INSERT INTO `t_apply_department` VALUES (274, 40, 9, b'0', 1, 1);

-- ----------------------------
-- Table structure for t_course_ware
-- ----------------------------
DROP TABLE IF EXISTS `t_course_ware`;
CREATE TABLE `t_course_ware`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `file_type` int NULL DEFAULT NULL COMMENT '课件类型：1.视频   2.文档',
  `original_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原始文档',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `preview_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览文件',
  `create_department_id` int NULL DEFAULT NULL,
  `course_ware_archive_id` int NULL DEFAULT NULL,
  `max_length` int NULL DEFAULT NULL COMMENT '课件时长',
  `vm_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '虚拟机环境',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course_ware
-- ----------------------------
INSERT INTO `t_course_ware` VALUES (1, '01 知识学习-Shell 脚本介绍', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/14/a4eff6e9-fea2-427d-a6bc-df08c0cfd547/01 知识学习-Shell 脚本介绍.pdf', '01 知识学习-Shell 脚本介绍.pdf', '01 知识学习-Shell 脚本介绍', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/14/a4eff6e9-fea2-427d-a6bc-df08c0cfd547/01 知识学习-Shell 脚本介绍.pdf', 1, 1, 600, 'kylin-desktop');
INSERT INTO `t_course_ware` VALUES (2, '02 知识学习-Shell脚本入门', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/14/e41732e0-3df1-4824-852f-cffae3e62605/02 知识学习-Shell脚本入门.pdf', '02 知识学习-Shell脚本入门.pdf', '02 知识学习-Shell脚本入门', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/14/e41732e0-3df1-4824-852f-cffae3e62605/02 知识学习-Shell脚本入门.pdf', 1, 1, 600, 'kylin-server');
INSERT INTO `t_course_ware` VALUES (3, '02-02 Shell应用', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/14/de9acdfc-c714-4ed9-a252-f93bbbb38b46/02-02 Shell应用.pdf', '02-02 Shell应用.pdf', '02-02 Shell应用', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/14/de9acdfc-c714-4ed9-a252-f93bbbb38b46/02-02 Shell应用.pdf', 1, 1, 180, NULL);
INSERT INTO `t_course_ware` VALUES (4, '03 知识学习-Shell介绍', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/faa747db-436e-4ffa-b099-cea14720435e/03 知识学习-Shell介绍.pdf', '03 知识学习-Shell介绍.pdf', '03 知识学习-Shell介绍', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/faa747db-436e-4ffa-b099-cea14720435e/03 知识学习-Shell介绍.pdf', 1, 1, 240, NULL);
INSERT INTO `t_course_ware` VALUES (5, '03-02 Shell介绍', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/665de274-c2d9-4187-bcc1-47e8aecb0bc5/03-02Shell介绍.pdf', '03-02Shell介绍.pdf', '03-02 Shell介绍', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/665de274-c2d9-4187-bcc1-47e8aecb0bc5/03-02Shell介绍.pdf', 1, 1, 120, NULL);
INSERT INTO `t_course_ware` VALUES (6, '03-03 Shell变量', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/7ec7ecd0-b8ec-4d9f-b1f1-d42347abe7b0/03-03 Shell变量.pdf', '03-03 Shell变量.pdf', '03-03 Shell变量', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/7ec7ecd0-b8ec-4d9f-b1f1-d42347abe7b0/03-03 Shell变量.pdf', 1, 1, 240, NULL);
INSERT INTO `t_course_ware` VALUES (7, '03-04 Shell第一个脚本', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/c604c8cc-a265-43a3-8041-bd49ff906885/03-04 Shell第一个脚本.pdf', '03-04 Shell第一个脚本.pdf', '03-04 Shell第一个脚本', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/c604c8cc-a265-43a3-8041-bd49ff906885/03-04 Shell第一个脚本.pdf', 1, 1, 180, NULL);
INSERT INTO `t_course_ware` VALUES (8, '03-05 Shell编程思想', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/5ed6da4d-b360-4d11-a819-c43f44584ecc/03-05 Shell编程思想.pdf', '03-05 Shell编程思想.pdf', '03-05 Shell编程思想', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/5ed6da4d-b360-4d11-a819-c43f44584ecc/03-05 Shell编程思想.pdf', 1, 1, 120, NULL);
INSERT INTO `t_course_ware` VALUES (9, '03-06 Shell数组', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/98e1531e-a612-438f-b6a6-0c8405fb87a8/03-06 Shell数组.pdf', '03-06 Shell数组.pdf', '03-06 Shell数组', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2023/12/18/98e1531e-a612-438f-b6a6-0c8405fb87a8/03-06 Shell数组.pdf', 1, 1, 120, NULL);
INSERT INTO `t_course_ware` VALUES (10, '视频-国产操作系统解析', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload/course/video/2023/12/18/6aa0fca4-6061-4938-bfc3-5cbdcbb02fb6/1-【麒麟软件】麒麟操作系统应用高级工程师【全50讲 持续更新】-720P 高清-AVC.mp4', '1-【麒麟软件】麒麟操作系统应用高级工程师【全50讲 持续更新】-720P 高清-AVC.mp4', '视频-国产操作系统解析', 'http://uejyptad.ueweixin.com/ueupload/course/video/2023/12/18/6aa0fca4-6061-4938-bfc3-5cbdcbb02fb6/1-【麒麟软件】麒麟操作系统应用高级工程师【全50讲 持续更新】-720P 高清-AVC.mp4', 1, 1, 1302, NULL);
INSERT INTO `t_course_ware` VALUES (11, '01-麒麟桌面操作系统介绍', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/68689b91-7c2f-4c7d-96ff-3c4059bb8dca/01-麒麟桌面操作系统介绍.pdf', '01-麒麟桌面操作系统介绍.pdf', '01-麒麟桌面操作系统介绍', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/68689b91-7c2f-4c7d-96ff-3c4059bb8dca/01-麒麟桌面操作系统介绍.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (12, '02-麒麟桌面操作系统安装', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/46629c0a-789c-4883-9563-af52c1615ea7/02-麒麟桌面操作系统安装.pdf', '02-麒麟桌面操作系统安装.pdf', '02-麒麟桌面操作系统安装', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/46629c0a-789c-4883-9563-af52c1615ea7/02-麒麟桌面操作系统安装.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (13, '03-系统登录及桌面环境', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/f44d71b3-0c29-4f76-b126-ff60c13adf8c/03-系统登录及桌面环境.pdf', '03-系统登录及桌面环境.pdf', '03-系统登录及桌面环境', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/f44d71b3-0c29-4f76-b126-ff60c13adf8c/03-系统登录及桌面环境.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (14, '04-系统管理工具', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/b101b864-12bc-48e4-8450-2faf670bdae7/04-系统管理工具.pdf', '04-系统管理工具.pdf', '04-系统管理工具', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/b101b864-12bc-48e4-8450-2faf670bdae7/04-系统管理工具.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (15, '05-系统设置', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/50bce559-85b1-4cfa-8712-1c02f2116715/05-系统设置.pdf', '05-系统设置.pdf', '05-系统设置', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/50bce559-85b1-4cfa-8712-1c02f2116715/05-系统设置.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (16, '06-网络设置', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/8f9077a0-d3cd-4c3d-8873-51b4389489c9/06-网络设置.pdf', '06-网络设置.pdf', '06-网络设置', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/8f9077a0-d3cd-4c3d-8873-51b4389489c9/06-网络设置.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (17, '07-用户设置', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/79597833-6b22-44c0-bde4-26d4fd7fa811/07-用户设置.pdf', '07-用户设置.pdf', '07-用户设置', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/79597833-6b22-44c0-bde4-26d4fd7fa811/07-用户设置.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (18, '08-安全中心', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/79b08571-be59-4a33-953f-a71885db0722/08-安全中心.pdf', '08-安全中心.pdf', '08-安全中心', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/79b08571-be59-4a33-953f-a71885db0722/08-安全中心.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (19, '09-KMRE', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/f86226b7-c18f-449f-892c-01c9fe27e79b/09-KMRE.pdf', '09-KMRE.pdf', '09-KMRE', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/19/f86226b7-c18f-449f-892c-01c9fe27e79b/09-KMRE.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (20, '1 容器云概述', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/c0772792-bd21-4093-b44d-50732a3edbd8/1 容器云概述.pdf', '1 容器云概述.pdf', '1 容器云概述', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/c0772792-bd21-4093-b44d-50732a3edbd8/1 容器云概述.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (21, '1-1 容器云管理课程', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/2e45fd12-3f6f-43c7-b115-45c6dae6ee8e/1-1 容器云管理课程.pdf', '1-1 容器云管理课程.pdf', '1-1 容器云管理课程', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/2e45fd12-3f6f-43c7-b115-45c6dae6ee8e/1-1 容器云管理课程.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (22, '1-2 容器云管理课程', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/b543fd37-e4bb-4495-a02a-952d092e4aed/1-2 容器云管理课程.pdf', '1-2 容器云管理课程.pdf', '1-2 容器云管理课程', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/b543fd37-e4bb-4495-a02a-952d092e4aed/1-2 容器云管理课程.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (23, '2 容器云部署', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/3784fa41-e4b5-41ec-9924-4eacae98eb24/2 容器云部署.pdf', '2 容器云部署.pdf', '2 容器云部署', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/3784fa41-e4b5-41ec-9924-4eacae98eb24/2 容器云部署.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (24, '2-1 容器云管理课程', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/02ae889e-894a-48c5-9754-ddbd32bfaf63/2-1 容器云管理课程.pdf', '2-1 容器云管理课程.pdf', '2-1 容器云管理课程', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/02ae889e-894a-48c5-9754-ddbd32bfaf63/2-1 容器云管理课程.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (25, '2-2 容器云管理课程', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/2fa2ff20-c2dd-4f97-b9f1-850360417e21/2-2 容器云管理课程.pdf', '2-2 容器云管理课程.pdf', '2-2 容器云管理课程', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/2fa2ff20-c2dd-4f97-b9f1-850360417e21/2-2 容器云管理课程.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (26, '1-1 初识麒麟', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/526e03cd-9104-4bdd-a3c3-80ef0396f7c5/1-1 初识麒麟.pdf', '1-1 初识麒麟.pdf', '1-1 初识麒麟', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/526e03cd-9104-4bdd-a3c3-80ef0396f7c5/1-1 初识麒麟.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (27, '1-2 初识麒麟', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/5eabcf04-a724-48d2-8345-9f00958e510e/1-2 初识麒麟.pdf', '1-2 初识麒麟.pdf', '1-2 初识麒麟', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/5eabcf04-a724-48d2-8345-9f00958e510e/1-2 初识麒麟.pdf', 1, 1, 300, NULL);
INSERT INTO `t_course_ware` VALUES (28, '2-1 系统安装与配置', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/8eb4fd21-4280-4d3d-a42e-66d046a1c75d/2-1 系统安装与配置.pdf', '2-1 系统安装与配置.pdf', '2-1 系统安装与配置', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/8eb4fd21-4280-4d3d-a42e-66d046a1c75d/2-1 系统安装与配置.pdf', 1, 1, 300, 'kylin-desktop');
INSERT INTO `t_course_ware` VALUES (29, '2-2 系统安装与配置', 1, '2025-12-14 14:32:57', b'0', 2, 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/357eda78-8262-417d-a330-40131ab457ea/2-2 系统安装与配置.pdf', '2-2 系统安装与配置.pdf', '2-2 系统安装与配置', 'http://uejyptad.ueweixin.com/ueupload/course/document/original/2025/01/22/357eda78-8262-417d-a330-40131ab457ea/2-2 系统安装与配置.pdf', 1, 1, 300, 'kylin-desktop');
INSERT INTO `t_course_ware` VALUES (30, '1-01-麒麟桌面操作系统介绍（麒麟操作系统的发展）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/0f2c1f04-1a08-4541-8635-1b59a2c6ab49/1-01-麒麟桌面操作系统介绍（麒麟操作系统的发展）.mp4', '1-01-麒麟桌面操作系统介绍（麒麟操作系统的发展）.mp4', '1-01-麒麟桌面操作系统介绍（麒麟操作系统的发展）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/0f2c1f04-1a08-4541-8635-1b59a2c6ab49/1-01-麒麟桌面操作系统介绍（麒麟操作系统的发展）.mp4', 1, 2, 887, NULL);
INSERT INTO `t_course_ware` VALUES (31, '1-02-麒麟桌面操作系统介绍（产品特色）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/920c9511-3255-470a-b0d0-96c2326a2088/1-02-麒麟桌面操作系统介绍（产品特色）.mp4', '1-02-麒麟桌面操作系统介绍（产品特色）.mp4', '1-02-麒麟桌面操作系统介绍（产品特色）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/920c9511-3255-470a-b0d0-96c2326a2088/1-02-麒麟桌面操作系统介绍（产品特色）.mp4', 1, 2, 582, NULL);
INSERT INTO `t_course_ware` VALUES (32, '1-03-麒麟桌面操作系统介绍（与Windows主要区别、核心竞争力）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/e785d2e0-8405-49a9-b915-0edc9a959b65/1-03-麒麟桌面操作系统介绍（与Windows主要区别、核心竞争力）.mp4', '1-03-麒麟桌面操作系统介绍（与Windows主要区别、核心竞争力）.mp4', '1-03-麒麟桌面操作系统介绍（与Windows主要区别、核心竞争力）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/e785d2e0-8405-49a9-b915-0edc9a959b65/1-03-麒麟桌面操作系统介绍（与Windows主要区别、核心竞争力）.mp4', 1, 2, 982, NULL);
INSERT INTO `t_course_ware` VALUES (33, '2-01-麒麟桌面操作系统安装-理论（安装前准备）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/c5114046-9272-495b-acc4-b24ee926dd56/2-01-麒麟桌面操作系统安装-理论（安装前准备）.mp4', '2-01-麒麟桌面操作系统安装-理论（安装前准备）.mp4', '2-01-麒麟桌面操作系统安装-理论（安装前准备）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/c5114046-9272-495b-acc4-b24ee926dd56/2-01-麒麟桌面操作系统安装-理论（安装前准备）.mp4', 1, 2, 845, NULL);
INSERT INTO `t_course_ware` VALUES (34, '2-02-麒麟桌面操作系统安装-实操（全盘安装）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/aeb37ad1-635a-44d0-a877-f9d4998b59e5/2-02-麒麟桌面操作系统安装-实操（全盘安装）.mp4', '2-02-麒麟桌面操作系统安装-实操（全盘安装）.mp4', '2-02-麒麟桌面操作系统安装-实操（全盘安装）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/aeb37ad1-635a-44d0-a877-f9d4998b59e5/2-02-麒麟桌面操作系统安装-实操（全盘安装）.mp4', 1, 2, 934, NULL);
INSERT INTO `t_course_ware` VALUES (35, '3-01-系统登录及桌面环境（系统登录、桌面使用）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/5c631b37-5711-418c-b830-b76acca45307/3-01-系统登录及桌面环境（系统登录、桌面使用）.mp4', '3-01-系统登录及桌面环境（系统登录、桌面使用）.mp4', '3-01-系统登录及桌面环境（系统登录、桌面使用）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/5c631b37-5711-418c-b830-b76acca45307/3-01-系统登录及桌面环境（系统登录、桌面使用）.mp4', 1, 2, 1066, NULL);
INSERT INTO `t_course_ware` VALUES (36, '3-02-系统登录及桌面环境（系统初始化设置）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/611cdf65-1ca5-4013-afac-6139957e54d6/3-02-系统登录及桌面环境（系统初始化设置）.mp4', '3-02-系统登录及桌面环境（系统初始化设置）.mp4', '3-02-系统登录及桌面环境（系统初始化设置）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/611cdf65-1ca5-4013-afac-6139957e54d6/3-02-系统登录及桌面环境（系统初始化设置）.mp4', 1, 2, 781, NULL);
INSERT INTO `t_course_ware` VALUES (37, '4-01-系统管理工具（软件商店、工具箱、备份还原工具）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/11d362ba-a037-413b-b21e-2b5056368e92/4-01-系统管理工具（软件商店、工具箱、备份还原工具）.mp4', '4-01-系统管理工具（软件商店、工具箱、备份还原工具）.mp4', '4-01-系统管理工具（软件商店、工具箱、备份还原工具）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/11d362ba-a037-413b-b21e-2b5056368e92/4-01-系统管理工具（软件商店、工具箱、备份还原工具）.mp4', 1, 2, 773, 'kylin-desktop');
INSERT INTO `t_course_ware` VALUES (38, '4-02-系统管理工具（U盘启动器、日志查看器）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/dbff49f0-95da-4392-957e-859f0692d6c0/4-02-系统管理工具（U盘启动器、日志查看器）.mp4', '4-02-系统管理工具（U盘启动器、日志查看器）.mp4', '4-02-系统管理工具（U盘启动器、日志查看器）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/dbff49f0-95da-4392-957e-859f0692d6c0/4-02-系统管理工具（U盘启动器、日志查看器）.mp4', 1, 2, 1149, NULL);
INSERT INTO `t_course_ware` VALUES (39, '5-01-系统设置', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/8b317f30-090f-4aa3-bafc-f5ae85cf6bed/5-01-系统设置.mp4', '5-01-系统设置.mp4', '5-01-系统设置', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/8b317f30-090f-4aa3-bafc-f5ae85cf6bed/5-01-系统设置.mp4', 1, 2, 751, NULL);
INSERT INTO `t_course_ware` VALUES (40, '5-02-系统设置实践', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/67416de0-d281-472c-baea-b97036b7cade/5-02-系统设置实践.mp4', '5-02-系统设置实践.mp4', '5-02-系统设置实践', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/67416de0-d281-472c-baea-b97036b7cade/5-02-系统设置实践.mp4', 1, 2, 840, NULL);
INSERT INTO `t_course_ware` VALUES (41, '5-03-网络设置（查看网络参数、配置静态网络）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/ec2a0229-008f-4f01-90c5-cbcbc3b7d4f1/5-03-网络设置（查看网络参数、配置静态网络）.mp4', '5-03-网络设置（查看网络参数、配置静态网络）.mp4', '5-03-网络设置（查看网络参数、配置静态网络）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/ec2a0229-008f-4f01-90c5-cbcbc3b7d4f1/5-03-网络设置（查看网络参数、配置静态网络）.mp4', 1, 2, 896, NULL);
INSERT INTO `t_course_ware` VALUES (42, '5-04-用户设置', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/e03c52e1-a000-4aba-832d-3881909f0378/5-04-用户设置.mp4', '5-04-用户设置.mp4', '5-04-用户设置', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/e03c52e1-a000-4aba-832d-3881909f0378/5-04-用户设置.mp4', 1, 2, 732, NULL);
INSERT INTO `t_course_ware` VALUES (43, '5-05-安全中心（安全体检、账户安全）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/d46ae657-7662-4d57-a3da-fd4daf98b3ad/5-05-安全中心（安全体检、账户安全）.mp4', '5-05-安全中心（安全体检、账户安全）.mp4', '5-05-安全中心（安全体检、账户安全）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/d46ae657-7662-4d57-a3da-fd4daf98b3ad/5-05-安全中心（安全体检、账户安全）.mp4', 1, 2, 911, NULL);
INSERT INTO `t_course_ware` VALUES (44, '6-01-麒麟移动虚拟化（KMRE介绍、优势、功能、核心技术）', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/bee1d552-9f3a-4870-99a5-c956140d3f21/6-01-麒麟移动虚拟化（KMRE介绍、优势、功能、核心技术）.mp4', '6-01-麒麟移动虚拟化（KMRE介绍、优势、功能、核心技术）.mp4', '6-01-麒麟移动虚拟化（KMRE介绍、优势、功能、核心技术）', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/04/18/bee1d552-9f3a-4870-99a5-c956140d3f21/6-01-麒麟移动虚拟化（KMRE介绍、优势、功能、核心技术）.mp4', 1, 2, 777, NULL);
INSERT INTO `t_course_ware` VALUES (45, '测试课件01', 1, '2025-12-14 14:32:57', b'1', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/05/06/54662027-29fe-42f3-b8ad-2d3815bde0f9/证书管理-模板列表-添加模板-内部错误.mp4', '证书管理-模板列表-添加模板-内部错误.mp4', '测试课件01', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/05/06/54662027-29fe-42f3-b8ad-2d3815bde0f9/证书管理-模板列表-添加模板-内部错误.mp4', 1, 3, 44, NULL);
INSERT INTO `t_course_ware` VALUES (46, '测试课件02', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/06/4786dea8-8c93-469c-b236-c0e48b3da845/测试文档.pdf', '测试文档.pdf', '测试课件02', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/06/4786dea8-8c93-469c-b236-c0e48b3da845/测试文档.pdf', 1, 3, 10, NULL);
INSERT INTO `t_course_ware` VALUES (47, '1', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/11/5fc1c6f5-578c-420c-9e6e-188a9eba27f6/测试工作流程.pdf', '测试工作流程.pdf', '1', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/11/5fc1c6f5-578c-420c-9e6e-188a9eba27f6/测试工作流程.pdf', 1, NULL, 12, 'kylin-server');
INSERT INTO `t_course_ware` VALUES (48, '12', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/11/c8ef98b5-1ec1-4178-bd01-64fcbbb9c018/测试工作流程.pdf', '测试工作流程.pdf', '12', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/11/c8ef98b5-1ec1-4178-bd01-64fcbbb9c018/测试工作流程.pdf', 1, 1, 12, 'kylin-server');
INSERT INTO `t_course_ware` VALUES (49, '1', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/f24b8d7c-84c5-4956-859f-a42b9bf0ca4c/工作流程.pdf', '工作流程.pdf', '1', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/f24b8d7c-84c5-4956-859f-a42b9bf0ca4c/工作流程.pdf', 1, 3, 12, 'kylin-server');
INSERT INTO `t_course_ware` VALUES (50, '11', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/1a112ab9-6265-4fe5-9768-570c8e6660bf/工作流程.pdf', '工作流程.pdf', '1', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/1a112ab9-6265-4fe5-9768-570c8e6660bf/工作流程.pdf', 1, 3, 60, 'kylin-server');
INSERT INTO `t_course_ware` VALUES (51, '2', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/b6f71702-6385-4341-8aa3-e29afd35517a/工作流程.pdf', '工作流程.pdf', '2', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/b6f71702-6385-4341-8aa3-e29afd35517a/工作流程.pdf', 1, 3, 600, NULL);
INSERT INTO `t_course_ware` VALUES (52, '112', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/6b1fa21d-a62a-4a8a-b93f-666bfb2f6eab/未命名1.pdf', '未命名1.pdf', '121', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/6b1fa21d-a62a-4a8a-b93f-666bfb2f6eab/未命名1.pdf', 1, 3, 60, NULL);
INSERT INTO `t_course_ware` VALUES (53, '123', 1, '2025-12-14 14:32:57', b'1', 2, 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/7d332bcc-1bf7-4d96-af4a-4aea686cb008/工作流程.pdf', '工作流程.pdf', '123', 'http://uejyptad.ueweixin.com/ueupload//course/document/original/2025/05/13/7d332bcc-1bf7-4d96-af4a-4aea686cb008/工作流程.pdf', 1, 3, 50, NULL);
INSERT INTO `t_course_ware` VALUES (54, '11', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/05/28/502be314-55ce-46aa-96c2-752af647c55d/第一章 Shell基础~3.mp4', '第一章 Shell基础~3.mp4', '11', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/05/28/502be314-55ce-46aa-96c2-752af647c55d/第一章 Shell基础~3.mp4', 1, NULL, 2773, NULL);
INSERT INTO `t_course_ware` VALUES (55, '计算机网络基础', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/10/22/f1a70338-3aa7-4df2-a18d-8b2168aeac0f/网络视频.mp4', '网络视频.mp4', '一、计算机在网络中的角色\n介绍计算机硬件中与网络相关的部件，如网卡等。讲解不同操作系统的网络配置及网络应用程序的工作原理。\n二、网络概述\n阐述网络的定义、分类及拓扑结构。介绍 OSI 和 TCP/IP 模型，讲解各层协议的作用。\n三、连接设备介绍\n包括网络接口卡、网络电缆与连接器、无线连接设备等，说明其特点和使用方法。\n四、交换机详解\n讲解交换机工作原理，如存储转发和 MAC 地址学习。介绍不同类型交换机的特点及配置管理。\n五、路由器深入剖析\n阐述路由器的功能，如 NAT 和路由选择。区分不同类型路由器，讲解其配置与维护。\n', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/10/22/f1a70338-3aa7-4df2-a18d-8b2168aeac0f/网络视频.mp4', 1, 1, 224, NULL);
INSERT INTO `t_course_ware` VALUES (56, '计算机网络基础第一讲', 1, '2025-12-14 14:32:57', b'0', 1, 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/10/22/94cf8346-7787-4235-b657-f226fd065a8d/网络视频.mp4', '网络视频.mp4', '一、课程目标\n让学生掌握计算机网络的基本概念、原理与体系结构，像网络分层模型等知识。\n培养学生网络设计、组建、管理和故障排除的基本能力。\n二、课程内容\n概述：包括定义、发展、应用，以及按地理范围和拓扑结构的分类。\n物理层：主要讲传输介质和信号传输原理。\n数据链路层：涉及帧封装、差错控制和介质访问控制。\n网络层：重点是 IP 协议和路由选择算法。\n传输层：介绍 TCP 和 UDP 协议。\n应用层：涵盖 HTTP、FTP 等应用协议。\n三、课程意义\n为学习网络安全、云计算等领域打基础，在互联网应用和网络工程等场景发挥关键作用。\n', 'http://uejyptad.ueweixin.com/ueupload//course/video/2025/10/22/94cf8346-7787-4235-b657-f226fd065a8d/网络视频.mp4', 1, 1, 224, NULL);

-- ----------------------------
-- Table structure for t_course_ware_archive
-- ----------------------------
DROP TABLE IF EXISTS `t_course_ware_archive`;
CREATE TABLE `t_course_ware_archive`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `parent_id` int NULL DEFAULT NULL,
  `level` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `item_order` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course_ware_archive
-- ----------------------------
INSERT INTO `t_course_ware_archive` VALUES (1, '学习资料', 1, '2023-12-14 14:31:40', b'0', NULL, '/学习资料/', 1, 10);
INSERT INTO `t_course_ware_archive` VALUES (2, '桌面操作系统', 1, '2025-04-18 17:50:22', b'0', NULL, '/桌面操作系统/', 1, 20);
INSERT INTO `t_course_ware_archive` VALUES (3, '测试课题', 1, '2025-04-29 11:59:34', b'0', NULL, '/测试课题/', 1, 30);
INSERT INTO `t_course_ware_archive` VALUES (4, '测试课题1', 1, '2025-04-29 11:59:37', b'1', 3, '/测试课题/测试课题1/', 1, 40);
INSERT INTO `t_course_ware_archive` VALUES (5, 'cs', 22, '2025-04-29 16:44:37', b'1', NULL, '/cs/', 1, 50);

-- ----------------------------
-- Table structure for t_course_ware_question
-- ----------------------------
DROP TABLE IF EXISTS `t_course_ware_question`;
CREATE TABLE `t_course_ware_question`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_ware_id` int NULL DEFAULT NULL,
  `question_id` bigint NULL DEFAULT NULL,
  `anchor_format` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `anchor_second` int NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `question_frame_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目内容Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course_ware_question
-- ----------------------------
INSERT INTO `t_course_ware_question` VALUES (1, 3, 3, '10', 10, 1, '2023-12-14 14:56:54', 1, b'1', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (2, 3, 3, '20', 20, 1, '2023-12-14 15:04:03', 1, b'1', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (3, 3, 3, '60', 60, 1, '2023-12-14 15:04:29', 1, b'1', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (4, 3, 3, '120', 120, 1, '2023-12-14 15:34:38', 1, b'0', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (5, 7, 16, '90', 90, 1, '2023-12-18 10:44:27', 1, b'0', 'b5292ef8-6931-4a37-8e5b-c6b1f89faa2c');
INSERT INTO `t_course_ware_question` VALUES (6, 9, 2, '60', 60, 1, '2023-12-18 11:11:51', 1, b'0', '89c9ede4-2a5d-4170-9394-6c8e2025e858');
INSERT INTO `t_course_ware_question` VALUES (7, 6, 1, '60', 60, 1, '2023-12-18 11:13:15', 1, b'1', '9d16dffc-829e-4ef2-9335-ba85a39596b3');
INSERT INTO `t_course_ware_question` VALUES (8, 6, 3, '120', 120, 1, '2023-12-18 11:13:15', 1, b'1', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (9, 6, 1, '60', 60, 1, '2023-12-18 16:06:09', 1, b'1', '9d16dffc-829e-4ef2-9335-ba85a39596b3');
INSERT INTO `t_course_ware_question` VALUES (10, 6, 3, '120', 120, 1, '2023-12-18 16:06:09', 1, b'1', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (11, 6, 1, '60', 60, 1, '2023-12-18 16:08:03', 1, b'1', '9d16dffc-829e-4ef2-9335-ba85a39596b3');
INSERT INTO `t_course_ware_question` VALUES (12, 6, 3, '80', 80, 1, '2023-12-18 16:08:03', 1, b'1', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (13, 6, 1, '60', 60, 1, '2023-12-18 16:08:57', 1, b'0', '9d16dffc-829e-4ef2-9335-ba85a39596b3');
INSERT INTO `t_course_ware_question` VALUES (14, 6, 3, '120', 120, 1, '2023-12-18 16:08:57', 1, b'0', 'abf170f3-a52c-4b2d-a3db-bf3cf6b06f73');
INSERT INTO `t_course_ware_question` VALUES (15, 29, 149, '', NULL, 1, '2025-04-08 11:19:56', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (16, 28, 149, '', NULL, 1, '2025-04-08 14:25:49', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (17, 28, 149, '', NULL, 1, '2025-04-08 14:28:21', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (18, 28, 149, '', NULL, 1, '2025-04-08 14:32:17', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (19, 28, 149, '', NULL, 1, '2025-04-08 15:00:23', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (20, 28, 149, '', NULL, 1, '2025-04-08 15:01:57', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (21, 27, 149, '', NULL, 1, '2025-04-08 15:49:17', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (22, 27, 149, '', NULL, 1, '2025-04-08 15:50:43', 1, b'0', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (23, 27, 145, '', NULL, 1, '2025-04-08 15:50:43', 1, b'0', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (24, 29, 149, '', NULL, 1, '2025-04-09 15:38:27', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (25, 28, 149, '', NULL, 1, '2025-04-09 15:41:03', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (26, 29, 149, '', NULL, 1, '2025-04-09 16:23:03', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (27, 29, 149, '', NULL, 1, '2025-04-09 16:23:48', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (28, 28, 149, '', NULL, 1, '2025-04-10 08:58:32', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (29, 28, 146, '', NULL, 1, '2025-04-10 08:58:32', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (30, 28, 146, '', NULL, 1, '2025-04-19 18:16:09', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (31, 28, 149, '', NULL, 1, '2025-04-19 18:16:09', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (32, 28, 146, '', NULL, 1, '2025-04-24 10:18:56', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (33, 28, 149, '', NULL, 1, '2025-04-24 10:18:56', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (34, 28, 146, '', NULL, 1, '2025-04-24 10:19:06', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (35, 28, 149, '', NULL, 1, '2025-04-24 10:19:06', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (36, 29, 149, '', NULL, 1, '2025-04-24 10:23:05', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (37, 28, 146, '', NULL, 1, '2025-04-24 10:22:42', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (38, 28, 149, '', NULL, 1, '2025-04-24 10:22:42', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (39, 28, 146, '', NULL, 1, '2025-04-24 10:38:58', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (40, 28, 149, '', NULL, 1, '2025-04-24 10:38:58', 1, b'1', '14cb7077-5f33-4174-9b71-5cad07e22141');
INSERT INTO `t_course_ware_question` VALUES (41, 37, 145, '', 0, 1, '2025-04-24 10:50:02', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (42, 37, 146, '', 0, 1, '2025-04-24 10:50:02', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (43, 28, 145, '', 0, 1, '2025-04-28 15:55:06', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (44, 28, 146, '', 0, 1, '2025-04-28 15:55:06', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (45, 29, 145, '', 0, 1, '2025-04-28 15:55:23', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (46, 29, 146, '', 0, 1, '2025-04-28 15:55:23', 1, b'1', 'd7cd2c62-e14f-42a8-82b2-e1dcd44b2785');
INSERT INTO `t_course_ware_question` VALUES (47, 37, 145, '', 0, 1, '2025-04-28 15:55:52', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (48, 28, 145, '', 0, 1, '2025-04-28 15:56:01', 1, b'0', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (49, 29, 145, '', 0, 1, '2025-04-28 15:56:06', 1, b'0', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (50, 45, 163, '00:00:20', 20, 1, '2025-05-06 15:01:11', 1, b'1', '9d705159-594f-4c68-bad0-6e2d355eb8eb');
INSERT INTO `t_course_ware_question` VALUES (51, 45, 163, '00:00:20', 20, 1, '2025-05-06 15:02:36', 1, b'0', '9d705159-594f-4c68-bad0-6e2d355eb8eb');
INSERT INTO `t_course_ware_question` VALUES (52, 45, 157, '00:00:30', 30, 1, '2025-05-06 15:02:36', 1, b'0', '2e22d06f-611d-48c4-acc4-c143ce0493a2');
INSERT INTO `t_course_ware_question` VALUES (53, 46, 157, '00:00:05', 5, 1, '2025-05-06 15:15:54', 1, b'1', '2e22d06f-611d-48c4-acc4-c143ce0493a2');
INSERT INTO `t_course_ware_question` VALUES (54, 46, 157, '00:00:05', 5, 1, '2025-05-06 15:16:06', 1, b'1', '2e22d06f-611d-48c4-acc4-c143ce0493a2');
INSERT INTO `t_course_ware_question` VALUES (55, 46, 157, '00:00:05', 5, 1, '2025-05-06 15:16:23', 1, b'0', '2e22d06f-611d-48c4-acc4-c143ce0493a2');
INSERT INTO `t_course_ware_question` VALUES (56, 44, 164, '00:03:00', 180, 1, '2025-05-07 14:27:12', 1, b'0', '56cf7aad-627e-4cd8-bc1d-173b48c6b48e');
INSERT INTO `t_course_ware_question` VALUES (57, 37, 145, '', 0, 1, '2025-05-08 16:39:50', 1, b'0', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (58, 37, 166, '', 0, 1, '2025-05-08 16:39:50', 1, b'0', '0308ccf4-c462-44ec-b53e-d6d5da0ba459');
INSERT INTO `t_course_ware_question` VALUES (59, 53, 164, '00:00:20', 20, 1, '2025-05-13 11:29:27', 1, b'0', '56cf7aad-627e-4cd8-bc1d-173b48c6b48e');
INSERT INTO `t_course_ware_question` VALUES (60, 1, 145, '', 0, 1, '2025-05-15 11:45:53', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (61, 1, 145, '', 0, 1, '2025-05-15 11:47:04', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (62, 1, 145, '', 0, 1, '2025-05-15 11:47:15', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (63, 1, 145, '', 0, 1, '2025-05-15 11:47:30', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (64, 1, 145, '', 0, 1, '2025-05-15 11:47:56', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (65, 1, 145, '', 0, 1, '2025-05-15 11:51:15', 1, b'1', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (66, 1, 145, '', NULL, 1, '2025-05-15 11:53:03', 1, b'0', '62eaa594-c184-49b2-b66b-72837561b68f');
INSERT INTO `t_course_ware_question` VALUES (67, 2, 182, '', NULL, 1, '2025-05-15 17:44:56', 1, b'0', 'c400721d-c39b-49d0-a4f2-3eeb25e0d3a7');
INSERT INTO `t_course_ware_question` VALUES (68, 55, 323, '1', 1, 1, '2025-10-22 14:41:08', 1, b'1', 'cbecb4dd-1757-4194-b7d9-4881549490a5');
INSERT INTO `t_course_ware_question` VALUES (69, 55, 189, '2', 2, 1, '2025-10-22 14:41:08', 1, b'1', '2a00c762-618b-4426-a23b-bffc46131754');
INSERT INTO `t_course_ware_question` VALUES (70, 55, 323, '2', 2, 1, '2025-10-22 14:41:36', 1, b'0', 'cbecb4dd-1757-4194-b7d9-4881549490a5');
INSERT INTO `t_course_ware_question` VALUES (71, 55, 189, '6', 6, 1, '2025-10-22 14:41:36', 1, b'0', '2a00c762-618b-4426-a23b-bffc46131754');

-- ----------------------------
-- Table structure for t_course_ware_watch
-- ----------------------------
DROP TABLE IF EXISTS `t_course_ware_watch`;
CREATE TABLE `t_course_ware_watch`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_ware_id` int NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `watch_total_length` bigint NULL DEFAULT NULL,
  `watch_current_time` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course_ware_watch
-- ----------------------------
INSERT INTO `t_course_ware_watch` VALUES (4, 1, 4, '2023-12-14 15:28:45', 7, 720, 960);
INSERT INTO `t_course_ware_watch` VALUES (5, 2, 4, '2023-12-14 15:32:09', 7, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (6, 3, 4, '2023-12-14 15:36:08', 7, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (11, 1, 5, '2023-12-18 11:31:29', 6, 60, 60);
INSERT INTO `t_course_ware_watch` VALUES (12, 2, 5, '2023-12-18 11:32:35', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (13, 3, 5, '2023-12-18 11:34:45', 6, 180, 60);
INSERT INTO `t_course_ware_watch` VALUES (14, 4, 5, '2023-12-18 11:40:10', 6, 240, 240);
INSERT INTO `t_course_ware_watch` VALUES (15, 5, 5, '2023-12-18 11:44:24', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (16, 6, 5, '2023-12-18 11:47:01', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (17, 7, 5, '2023-12-18 14:04:42', 6, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (18, 8, 5, '2023-12-18 14:08:28', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (19, 9, 5, '2023-12-18 14:11:00', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (20, 10, 5, '2023-12-18 14:22:11', 6, 600, 600);
INSERT INTO `t_course_ware_watch` VALUES (26, 39, 11, '2025-04-30 17:19:04', 11, 540, 540);
INSERT INTO `t_course_ware_watch` VALUES (27, 39, 8, '2025-04-30 17:32:32', 11, 420, 420);
INSERT INTO `t_course_ware_watch` VALUES (28, 27, 11, '2025-05-06 14:41:14', 11, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (29, 27, 14, '2025-05-06 14:48:50', 11, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (30, 45, 11, '2025-05-06 15:36:41', 11, 180, 0);
INSERT INTO `t_course_ware_watch` VALUES (31, 45, 13, '2025-05-06 15:42:35', 11, 60, 45);
INSERT INTO `t_course_ware_watch` VALUES (32, 45, 12, '2025-05-06 15:46:39', 11, 60, 45);
INSERT INTO `t_course_ware_watch` VALUES (33, 45, 10, '2025-05-06 15:51:03', 11, 60, 45);
INSERT INTO `t_course_ware_watch` VALUES (34, 18, 10, '2025-05-07 11:57:38', 11, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (35, 44, 10, '2025-05-07 14:30:23', 11, 360, 360);
INSERT INTO `t_course_ware_watch` VALUES (37, 29, 10, '2025-05-07 14:51:32', 11, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (39, 30, 4, '2025-05-11 11:04:04', 7, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (40, 44, 11, '2025-05-14 10:43:45', 11, 60, 60);
INSERT INTO `t_course_ware_watch` VALUES (41, 1, 2, '2025-05-15 11:56:43', 6, 2880, 420);
INSERT INTO `t_course_ware_watch` VALUES (42, 2, 2, '2025-05-20 18:17:29', 6, 840, 120);
INSERT INTO `t_course_ware_watch` VALUES (43, 26, 2, '2025-05-31 15:50:29', 6, 600, 360);
INSERT INTO `t_course_ware_watch` VALUES (44, 27, 2, '2025-05-31 15:55:10', 6, 180, 180);
INSERT INTO `t_course_ware_watch` VALUES (45, 28, 2, '2025-05-31 15:58:16', 6, 300, 120);
INSERT INTO `t_course_ware_watch` VALUES (46, 29, 2, '2025-05-31 16:01:21', 6, 660, 660);
INSERT INTO `t_course_ware_watch` VALUES (47, 3, 10, '2025-08-21 15:03:45', 11, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (48, 4, 10, '2025-08-21 15:09:02', 11, 240, 180);
INSERT INTO `t_course_ware_watch` VALUES (49, 3, 30, '2025-08-22 10:18:15', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (50, 5, 30, '2025-08-22 10:18:59', 6, 120, 120);
INSERT INTO `t_course_ware_watch` VALUES (51, 30, 2, '2025-08-28 09:56:07', 6, 720, 720);
INSERT INTO `t_course_ware_watch` VALUES (52, 56, 2, '2025-10-22 17:22:28', 6, 60, 60);
INSERT INTO `t_course_ware_watch` VALUES (53, 3, 2, '2025-11-14 20:31:58', 6, 660, 120);
INSERT INTO `t_course_ware_watch` VALUES (54, 4, 2, '2025-11-15 14:26:30', 6, 1140, 960);
INSERT INTO `t_course_ware_watch` VALUES (55, 11, 2, '2025-12-05 20:25:44', 6, 600, 600);
INSERT INTO `t_course_ware_watch` VALUES (56, 8, 2, '2025-03-11 17:34:45', 6, 240, 240);
INSERT INTO `t_course_ware_watch` VALUES (57, 7, 2, '2025-03-22 08:26:42', 6, 120, 60);

-- ----------------------------
-- Table structure for t_course_ware_watch_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_course_ware_watch_detail`;
CREATE TABLE `t_course_ware_watch_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_ware_id` int NULL DEFAULT NULL,
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `watch_interval` int NULL DEFAULT NULL COMMENT '观看间隔（秒）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 782 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course_ware_watch_detail
-- ----------------------------
INSERT INTO `t_course_ware_watch_detail` VALUES (28, 1, 4, '2023-12-14 15:28:45', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (29, 1, 4, '2023-12-14 15:30:13', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (30, 2, 4, '2023-12-14 15:32:09', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (31, 2, 4, '2023-12-14 15:33:21', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (32, 3, 4, '2023-12-14 15:36:08', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (33, 3, 4, '2023-12-14 15:37:09', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (34, 3, 4, '2023-12-14 15:38:27', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (54, 1, 5, '2023-12-18 11:31:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (55, 2, 5, '2023-12-18 11:32:35', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (56, 2, 5, '2023-12-18 11:33:35', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (57, 3, 5, '2023-12-18 11:34:45', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (58, 3, 5, '2023-12-18 11:35:45', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (59, 3, 5, '2023-12-18 11:38:48', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (60, 4, 5, '2023-12-18 11:40:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (61, 4, 5, '2023-12-18 11:41:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (62, 4, 5, '2023-12-18 11:42:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (63, 4, 5, '2023-12-18 11:43:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (64, 5, 5, '2023-12-18 11:44:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (65, 5, 5, '2023-12-18 11:45:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (66, 6, 5, '2023-12-18 11:47:01', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (67, 6, 5, '2023-12-18 11:48:15', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (68, 7, 5, '2023-12-18 14:04:42', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (69, 7, 5, '2023-12-18 14:06:04', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (70, 7, 5, '2023-12-18 14:07:04', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (71, 8, 5, '2023-12-18 14:08:28', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (72, 8, 5, '2023-12-18 14:09:28', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (73, 9, 5, '2023-12-18 14:11:00', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (74, 9, 5, '2023-12-18 14:12:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (75, 10, 5, '2023-12-18 14:22:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (76, 10, 5, '2023-12-18 14:23:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (77, 10, 5, '2023-12-18 14:24:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (78, 10, 5, '2023-12-18 14:25:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (79, 10, 5, '2023-12-18 14:26:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (80, 10, 5, '2023-12-18 14:27:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (81, 10, 5, '2023-12-18 14:28:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (82, 10, 5, '2023-12-18 14:29:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (83, 10, 5, '2023-12-18 14:30:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (84, 10, 5, '2023-12-18 14:31:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (216, 1, 4, '2025-04-08 15:35:58', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (217, 1, 4, '2025-04-08 15:36:58', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (218, 1, 4, '2025-04-08 15:37:58', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (219, 1, 4, '2025-04-08 15:38:58', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (220, 1, 4, '2025-04-08 15:39:58', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (327, 1, 4, '2025-04-08 18:04:35', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (328, 1, 4, '2025-04-08 18:05:35', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (330, 1, 4, '2025-04-08 18:06:35', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (332, 1, 4, '2025-04-08 18:07:35', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (554, 39, 11, '2025-04-30 17:19:04', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (555, 39, 11, '2025-04-30 17:20:04', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (556, 39, 11, '2025-04-30 17:21:04', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (557, 39, 11, '2025-04-30 17:22:04', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (558, 39, 11, '2025-04-30 17:23:04', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (559, 39, 11, '2025-04-30 17:24:04', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (560, 39, 11, '2025-04-30 17:27:00', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (561, 39, 11, '2025-04-30 17:28:00', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (562, 39, 11, '2025-04-30 17:29:00', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (563, 39, 8, '2025-04-30 17:32:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (564, 39, 8, '2025-04-30 17:33:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (565, 39, 8, '2025-04-30 17:34:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (566, 39, 8, '2025-04-30 17:43:38', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (567, 39, 8, '2025-04-30 17:44:37', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (568, 39, 8, '2025-04-30 17:45:37', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (569, 39, 8, '2025-04-30 17:46:50', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (570, 27, 11, '2025-05-06 14:41:14', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (571, 27, 11, '2025-05-06 14:42:14', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (572, 27, 11, '2025-05-06 14:43:14', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (573, 27, 14, '2025-05-06 14:48:50', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (574, 27, 14, '2025-05-06 14:49:50', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (575, 27, 14, '2025-05-06 14:50:50', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (576, 45, 11, '2025-05-06 15:36:41', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (577, 45, 11, '2025-05-06 15:37:33', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (578, 45, 11, '2025-05-06 15:38:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (579, 45, 13, '2025-05-06 15:42:35', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (580, 45, 12, '2025-05-06 15:46:39', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (581, 45, 10, '2025-05-06 15:51:03', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (582, 18, 10, '2025-05-07 11:57:38', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (583, 18, 10, '2025-05-07 11:58:38', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (584, 18, 10, '2025-05-07 11:59:38', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (585, 44, 10, '2025-05-07 14:30:23', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (586, 44, 10, '2025-05-07 14:31:26', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (587, 44, 10, '2025-05-07 14:32:35', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (594, 44, 10, '2025-05-07 14:45:49', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (595, 44, 10, '2025-05-07 14:46:49', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (596, 44, 10, '2025-05-07 14:47:49', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (597, 29, 10, '2025-05-07 14:51:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (598, 29, 10, '2025-05-07 14:52:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (599, 29, 10, '2025-05-07 14:53:32', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (607, 30, 4, '2025-05-11 11:04:04', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (608, 30, 4, '2025-05-11 11:05:04', 7, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (621, 44, 11, '2025-05-14 10:43:45', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (622, 1, 2, '2025-05-15 11:56:43', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (623, 1, 2, '2025-05-15 11:57:55', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (624, 1, 2, '2025-05-15 12:32:25', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (625, 1, 2, '2025-05-15 13:32:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (626, 1, 2, '2025-05-15 14:50:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (627, 1, 2, '2025-05-15 14:51:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (628, 1, 2, '2025-05-15 15:08:25', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (629, 1, 2, '2025-05-15 15:09:25', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (630, 1, 2, '2025-05-15 15:39:43', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (631, 1, 2, '2025-05-15 15:40:43', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (632, 1, 2, '2025-05-15 15:41:49', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (633, 1, 2, '2025-05-15 15:42:49', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (634, 1, 2, '2025-05-15 15:45:00', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (635, 1, 2, '2025-05-15 15:47:52', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (636, 1, 2, '2025-05-15 16:40:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (637, 1, 2, '2025-05-15 17:40:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (638, 1, 2, '2025-05-20 11:04:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (639, 1, 2, '2025-05-20 11:05:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (640, 1, 2, '2025-05-20 11:06:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (641, 1, 2, '2025-05-20 11:07:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (642, 1, 2, '2025-05-20 11:08:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (643, 1, 2, '2025-05-20 11:09:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (644, 1, 2, '2025-05-20 11:17:46', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (645, 1, 2, '2025-05-20 11:18:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (646, 1, 2, '2025-05-20 11:19:46', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (647, 1, 2, '2025-05-20 11:20:46', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (648, 1, 2, '2025-05-20 14:57:11', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (649, 1, 2, '2025-05-20 14:59:38', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (650, 1, 2, '2025-05-20 15:00:38', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (651, 1, 2, '2025-05-20 15:14:39', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (652, 1, 2, '2025-05-20 17:19:06', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (653, 1, 2, '2025-05-20 17:20:06', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (654, 1, 2, '2025-05-20 17:22:34', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (655, 1, 2, '2025-05-20 17:36:06', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (656, 1, 2, '2025-05-20 17:37:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (657, 1, 2, '2025-05-20 17:48:19', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (658, 1, 2, '2025-05-20 17:54:32', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (659, 1, 2, '2025-05-20 17:55:32', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (660, 1, 2, '2025-05-20 18:03:26', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (661, 1, 2, '2025-05-20 18:05:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (662, 1, 2, '2025-05-20 18:07:43', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (663, 2, 2, '2025-05-20 18:17:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (664, 2, 2, '2025-05-20 18:18:33', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (665, 2, 2, '2025-05-20 18:19:33', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (666, 26, 2, '2025-05-31 15:50:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (667, 26, 2, '2025-05-31 15:51:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (668, 26, 2, '2025-05-31 15:52:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (669, 26, 2, '2025-05-31 15:53:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (670, 27, 2, '2025-05-31 15:55:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (671, 27, 2, '2025-05-31 15:56:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (672, 27, 2, '2025-05-31 15:57:10', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (673, 28, 2, '2025-05-31 15:58:16', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (674, 28, 2, '2025-05-31 15:59:16', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (675, 28, 2, '2025-05-31 16:00:16', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (676, 29, 2, '2025-05-31 16:01:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (677, 29, 2, '2025-05-31 16:02:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (678, 29, 2, '2025-05-31 16:03:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (679, 29, 2, '2025-05-31 16:04:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (680, 29, 2, '2025-05-31 16:05:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (681, 29, 2, '2025-05-31 16:06:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (682, 29, 2, '2025-05-31 16:07:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (683, 29, 2, '2025-05-31 16:08:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (684, 29, 2, '2025-05-31 16:09:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (685, 29, 2, '2025-05-31 16:10:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (686, 29, 2, '2025-05-31 16:11:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (687, 3, 10, '2025-08-21 15:03:45', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (688, 3, 10, '2025-08-21 15:04:44', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (689, 4, 10, '2025-08-21 15:09:02', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (690, 4, 10, '2025-08-21 15:15:02', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (691, 4, 10, '2025-08-21 16:12:59', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (692, 4, 10, '2025-08-21 17:12:59', 11, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (693, 3, 30, '2025-08-22 10:18:15', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (694, 5, 30, '2025-08-22 10:18:59', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (695, 5, 30, '2025-08-22 10:19:59', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (696, 3, 30, '2025-08-22 10:25:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (697, 30, 2, '2025-08-28 09:56:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (698, 30, 2, '2025-08-28 09:57:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (699, 30, 2, '2025-08-28 09:58:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (700, 30, 2, '2025-08-28 09:59:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (701, 30, 2, '2025-08-28 10:00:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (702, 30, 2, '2025-08-28 10:01:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (703, 30, 2, '2025-08-28 10:02:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (704, 30, 2, '2025-08-28 10:03:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (705, 30, 2, '2025-08-28 10:04:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (706, 30, 2, '2025-08-28 10:05:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (707, 30, 2, '2025-08-28 10:06:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (708, 30, 2, '2025-08-28 10:07:07', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (709, 56, 2, '2025-10-22 17:22:28', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (710, 3, 2, '2025-11-14 20:31:58', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (711, 3, 2, '2025-11-14 20:32:58', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (712, 3, 2, '2025-11-14 20:34:33', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (713, 4, 2, '2025-11-15 14:26:30', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (714, 4, 2, '2025-11-15 14:28:30', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (715, 4, 2, '2025-11-15 14:30:30', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (716, 11, 2, '2025-12-05 20:25:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (717, 11, 2, '2025-12-05 20:26:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (718, 11, 2, '2025-12-05 20:27:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (719, 11, 2, '2025-12-05 20:28:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (720, 11, 2, '2025-12-05 20:29:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (721, 11, 2, '2025-12-05 20:30:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (722, 11, 2, '2025-12-05 20:31:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (723, 11, 2, '2025-12-05 20:32:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (724, 11, 2, '2025-12-05 20:33:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (725, 11, 2, '2025-12-05 20:34:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (726, 8, 2, '2025-03-11 17:34:45', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (727, 8, 2, '2025-03-11 17:35:45', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (728, 8, 2, '2025-03-11 17:36:45', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (729, 8, 2, '2025-03-11 17:37:45', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (730, 3, 2, '2025-03-17 22:25:18', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (731, 3, 2, '2025-03-17 22:36:35', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (732, 3, 2, '2025-03-17 23:25:20', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (733, 3, 2, '2025-03-17 23:41:47', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (734, 2, 2, '2025-03-17 23:41:55', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (735, 3, 2, '2025-03-18 00:00:44', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (736, 2, 2, '2025-03-18 00:00:53', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (737, 3, 2, '2025-03-18 00:47:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (738, 2, 2, '2025-03-18 00:48:21', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (739, 7, 2, '2025-03-22 08:26:42', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (740, 4, 2, '2025-03-22 08:35:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (741, 4, 2, '2025-03-22 08:36:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (742, 4, 2, '2025-03-22 08:37:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (743, 4, 2, '2025-03-22 08:38:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (744, 4, 2, '2025-03-22 08:39:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (745, 4, 2, '2025-03-22 08:40:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (746, 4, 2, '2025-03-22 08:41:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (747, 4, 2, '2025-03-22 08:42:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (748, 4, 2, '2025-03-22 08:43:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (749, 4, 2, '2025-03-22 08:44:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (750, 4, 2, '2025-03-22 08:45:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (751, 4, 2, '2025-03-22 08:46:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (752, 4, 2, '2025-03-22 08:47:37', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (753, 2, 2, '2025-03-22 08:57:58', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (754, 2, 2, '2025-03-22 10:35:46', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (755, 4, 2, '2025-03-22 10:37:14', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (756, 2, 2, '2025-03-22 10:37:17', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (757, 4, 2, '2025-03-22 11:04:04', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (758, 4, 2, '2025-03-22 11:19:01', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (759, 3, 2, '2025-04-11 19:55:49', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (760, 3, 2, '2025-04-11 20:55:50', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (761, 7, 2, '2025-04-16 22:45:30', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (762, 2, 2, '2025-04-16 22:46:25', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (763, 28, 2, '2025-04-16 22:47:35', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (764, 28, 2, '2025-04-16 22:51:56', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (765, 2, 2, '2025-04-17 16:36:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (766, 2, 2, '2025-04-17 16:37:24', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (767, 2, 2, '2025-04-17 17:09:29', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (768, 2, 2, '2025-04-17 17:11:42', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (769, 26, 2, '2025-04-25 09:01:54', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (770, 26, 2, '2025-04-25 09:02:54', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (771, 26, 2, '2025-04-25 09:03:54', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (772, 26, 2, '2025-04-25 09:04:55', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (773, 26, 2, '2025-04-25 09:06:30', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (774, 26, 2, '2025-04-25 09:07:30', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (775, 1, 2, '2025-04-25 15:22:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (776, 1, 2, '2025-04-25 15:23:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (777, 1, 2, '2025-04-25 15:24:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (778, 1, 2, '2025-04-25 15:25:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (779, 1, 2, '2025-04-25 15:26:13', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (780, 1, 2, '2025-04-25 15:27:39', 6, 60);
INSERT INTO `t_course_ware_watch_detail` VALUES (781, 1, 2, '2025-04-25 15:38:27', 6, 60);

-- ----------------------------
-- Table structure for t_credential_template
-- ----------------------------
DROP TABLE IF EXISTS `t_credential_template`;
CREATE TABLE `t_credential_template`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板名称',
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `template_image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板图片地址',
  `create_user` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `create_department_id` int NULL DEFAULT NULL,
  `configuration` json NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_credential_template
-- ----------------------------
INSERT INTO `t_credential_template` VALUES (1, '课程结业证书模板', 'xx学校', 'http://uejyptad.ueweixin.com/ueupload/credential/2023/12/18/aa1ab4a0-4086-4e97-a0da-31e9e254c874/微信图片_20231218102252.jpg', 1, '2025-08-06 11:37:17', b'0', 1, '[{\"key\": \"e68b61a5-61f3-4628-b967-94ab439d254e\", \"text\": \"课程名称\", \"type\": 8, \"elementX\": 288, \"elementY\": 397, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"397px\", \"fontWeight\": 1, \"elementLeft\": \"288px\", \"validityMonth\": null}, {\"key\": \"9b487d67-ea95-410b-bea7-68db187ae1a4\", \"text\": \"鉴于您在\", \"type\": 7, \"elementX\": 182, \"elementY\": 398, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"398px\", \"fontWeight\": 1, \"elementLeft\": \"182px\", \"validityMonth\": null}, {\"key\": \"15b43809-ba6e-415f-ba12-6ed647451650\", \"text\": \"经考核合格，予以结业，特发此证。\", \"type\": 7, \"elementX\": 599, \"elementY\": 453, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"453px\", \"fontWeight\": 1, \"elementLeft\": \"599px\", \"validityMonth\": null}, {\"key\": \"813dedd6-ecf6-49de-84c4-55a9467f7a6b\", \"text\": \"创建时间\", \"type\": 4, \"elementX\": 662, \"elementY\": 545, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"545px\", \"fontWeight\": 1, \"elementLeft\": \"662px\", \"validityMonth\": null}, {\"key\": \"b07ca245-c924-4273-99d7-4075d8f61f59\", \"text\": \"证书编号\", \"type\": 3, \"elementX\": 240, \"elementY\": 543, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"543px\", \"fontWeight\": 1, \"elementLeft\": \"240px\", \"validityMonth\": null}, {\"key\": \"7b548f68-33f4-464f-94d4-15d029db06ca\", \"text\": \"电子印章\", \"type\": 6, \"elementX\": 824, \"elementY\": 529, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"529px\", \"fontWeight\": 1, \"elementLeft\": \"824px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (2, '考试模板', 'xx学校', 'http://uejyptad.ueweixin.com/ueupload/credential/2023/12/18/e8de3a34-c562-4e52-8364-abbf69cf209a/微信图片_20231218102252.jpg', 1, '2025-08-06 11:37:17', b'0', 1, '[{\"key\": \"752f7108-2005-4d0e-ac56-6c55dfd2c7a9\", \"text\": \"试卷名称\", \"type\": 1, \"elementX\": 301, \"elementY\": 395, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"395px\", \"fontWeight\": 1, \"elementLeft\": \"301px\", \"validityMonth\": null}, {\"key\": \"90ef1851-0ed2-494d-934c-6e1c31fd5b6e\", \"text\": \"创建时间\", \"type\": 4, \"elementX\": 667, \"elementY\": 546, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"546px\", \"fontWeight\": 1, \"elementLeft\": \"667px\", \"validityMonth\": null}, {\"key\": \"00baec20-f6bb-435b-b001-da3746ebe347\", \"text\": \"学员姓名\", \"type\": 2, \"elementX\": 174, \"elementY\": 395, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"395px\", \"fontWeight\": 1, \"elementLeft\": \"174px\", \"validityMonth\": null}, {\"key\": \"c77f40cf-c322-449e-9ca2-98d4e1ac2c40\", \"text\": \"在\", \"type\": 7, \"elementX\": 267, \"elementY\": 395, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"395px\", \"fontWeight\": 1, \"elementLeft\": \"267px\", \"validityMonth\": null}, {\"key\": \"b3c3d37d-efd1-4ae5-af8d-63596d35e3ef\", \"text\": \"证书编号\", \"type\": 3, \"elementX\": 241, \"elementY\": 541, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"541px\", \"fontWeight\": 1, \"elementLeft\": \"241px\", \"validityMonth\": null}, {\"key\": \"82924673-4dad-4fee-b4da-7400a3189cee\", \"text\": \"电子印章\", \"type\": 6, \"elementX\": 821, \"elementY\": 529, \"fontSize\": 22, \"fontColor\": \"#FF0000\", \"elementTop\": \"529px\", \"fontWeight\": 1, \"elementLeft\": \"821px\", \"validityMonth\": null}, {\"key\": \"aa663db0-dd08-4214-969a-b51cd3d34e62\", \"text\": \"考试成绩合格，特发此证。\", \"type\": 7, \"elementX\": 688, \"elementY\": 461, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"461px\", \"fontWeight\": 1, \"elementLeft\": \"688px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (3, '01', '考试模板证书', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/ceedd09a-8db2-4600-8e72-5300d1f80d07/2.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"4e9e0ddd-9219-4f63-a413-94c104c8c601\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 71, \"elementY\": 110, \"fontSize\": 22, \"fontColor\": \"#D90F0F\", \"elementTop\": \"110px\", \"fontWeight\": 1, \"elementLeft\": \"71px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (4, '02', '02', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/aa880ea7-2c4f-45b0-8040-0d93478f1786/4.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"557c4d20-a94d-42c1-98ea-4dd5c19f2a4b\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 55, \"elementY\": 131, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"131px\", \"fontWeight\": 1, \"elementLeft\": \"55px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (5, '03', '03', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/d7798172-2e77-484e-a43f-f56f9c152fc1/4.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"38ae8f17-9e70-40c4-941c-90d5999ddc9e\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 0, \"elementY\": 75, \"fontSize\": 58, \"fontColor\": \"#4AE232\", \"elementTop\": \"75px\", \"fontWeight\": 1, \"elementLeft\": \"0px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (6, '04', '04', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/9aee00c6-427e-4f72-89e0-a1b718098042/4.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"fcaeed50-2cac-4974-99bf-d033f4095e68\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 33, \"elementY\": 100, \"fontSize\": 30, \"fontColor\": \"#163BCD\", \"elementTop\": \"101px\", \"fontWeight\": 1, \"elementLeft\": \"34px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (7, '05', '05', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/633e9f9c-02b9-4d85-9a3f-af57af3578aa/4.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"a90525d5-37d9-4d77-a321-4064289cd16e\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 8, \"elementY\": 72, \"fontSize\": 38, \"fontColor\": \"#000000\", \"elementTop\": \"73px\", \"fontWeight\": 1, \"elementLeft\": \"9px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (8, '06', '06', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/395655eb-308c-429b-aa3d-67ddf9dfac34/2.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"f0b0b58e-d678-4320-b574-43586545c7af\", \"text\": \"课程名称\", \"type\": 8, \"elementX\": 50, \"elementY\": 120, \"fontSize\": 36, \"fontColor\": \"#000000\", \"elementTop\": \"121px\", \"fontWeight\": 1, \"elementLeft\": \"50px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (9, '07', '07', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/15f106d7-3844-470a-9937-ac7e0dd1a09f/2.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"cb51cc13-92aa-4466-b002-dbc909f443b9\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 72, \"elementY\": 112, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"113px\", \"fontWeight\": 1, \"elementLeft\": \"73px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (10, '08', '08', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/7d87210f-9ac9-4c30-acab-b6bedd45ba58/2.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"d829cb5f-1381-4e47-b64b-56340052d701\", \"text\": \"考试名称\", \"type\": 1, \"elementX\": 45, \"elementY\": 120, \"fontSize\": 40, \"fontColor\": \"#000000\", \"elementTop\": \"121px\", \"fontWeight\": 1, \"elementLeft\": \"46px\", \"validityMonth\": null}]');
INSERT INTO `t_credential_template` VALUES (11, '09', '09', 'http://uejyptad.ueweixin.com/ueupload//credential/2025/05/06/08f01884-e1c3-4114-948d-3d64ec4afed3/2.png', 1, '2025-08-06 11:37:17', b'1', 1, '[{\"key\": \"f4aad6f5-b5de-435e-960e-834e87d694e4\", \"text\": \"创建时间\", \"type\": 4, \"elementX\": 13, \"elementY\": 55, \"fontSize\": 22, \"fontColor\": \"#000000\", \"elementTop\": \"55px\", \"fontWeight\": 1, \"elementLeft\": \"14px\", \"validityMonth\": null}]');

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门名称',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  `parent_id` int NULL DEFAULT NULL COMMENT '父级Id',
  `level` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门层级',
  `create_department_id` int NULL DEFAULT NULL,
  `item_order` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES (1, '忒伊亚', 1, '2023-12-11 16:20:19', b'0', NULL, '/忒伊亚/', NULL, 10);
INSERT INTO `t_department` VALUES (2, '研发部', 1, '2023-12-11 16:20:36', b'0', 1, '/忒伊亚/研发部/', NULL, 20);
INSERT INTO `t_department` VALUES (3, '北京信息科技大学', 1, '2023-12-14 14:35:28', b'0', NULL, '/北京信息科技大学/', 1, 30);
INSERT INTO `t_department` VALUES (4, '软件工程', 1, '2023-12-14 14:35:37', b'1', 3, '/北京信息科技大学/软件工程/', 1, 40);
INSERT INTO `t_department` VALUES (5, '计算机学院', 1, '2023-12-14 14:36:37', b'0', 3, '/北京信息科技大学/计算机学院/', 1, 50);
INSERT INTO `t_department` VALUES (6, '软件工程', 1, '2023-12-14 14:36:49', b'0', 5, '/北京信息科技大学/计算机学院/软件工程/', 1, 60);
INSERT INTO `t_department` VALUES (7, '计算机科学与技术', 1, '2023-12-14 14:36:58', b'0', 5, '/北京信息科技大学/计算机学院/计算机科学与技术/', 1, 70);
INSERT INTO `t_department` VALUES (8, '物联网工程', 1, '2023-12-14 14:37:04', b'0', 5, '/北京信息科技大学/计算机学院/物联网工程/', 1, 80);
INSERT INTO `t_department` VALUES (9, '电子科学与技术', 1, '2023-12-28 14:48:25', b'0', 5, '/北京信息科技大学/计算机学院/电子科学与技术/', 1, 90);
INSERT INTO `t_department` VALUES (10, '北京大学', 1, '2025-04-29 10:29:58', b'0', NULL, '/北京大学/', 1, 100);
INSERT INTO `t_department` VALUES (11, '功能测试', 1, '2025-04-29 10:32:35', b'0', 10, '/北京大学/功能测试/', 1, 110);
INSERT INTO `t_department` VALUES (12, '功能点', 1, '2025-04-29 10:33:04', b'1', 11, '/北京大学/功能测试/功能点/', 1, 120);
INSERT INTO `t_department` VALUES (13, '测试01', 1, '2025-04-29 10:47:10', b'1', 11, '/北京大学/功能测试/测试01/', 1, 130);
INSERT INTO `t_department` VALUES (14, '功能测试', 1, '2025-04-29 11:28:40', b'0', 11, '/北京大学/功能测试/功能测试/', 1, 140);
INSERT INTO `t_department` VALUES (15, '西安美术学院', 1, '2025-06-12 10:00:59', b'0', NULL, '/西安美术学院/', 1, 150);
INSERT INTO `t_department` VALUES (16, '实训学习', 1, '2025-06-12 10:01:34', b'0', 15, '/西安美术学院/实训学习/', 1, 160);

-- ----------------------------
-- Table structure for t_exam_paper
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_paper`;
CREATE TABLE `t_exam_paper`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `paper_type` int NULL DEFAULT NULL COMMENT '试卷类型(人工组卷、抽题组卷、随机组卷)',
  `score` int NULL DEFAULT NULL COMMENT '试卷分数',
  `question_count` int NULL DEFAULT NULL COMMENT '题目数量',
  `suggest_time` int NULL DEFAULT NULL COMMENT '考试时长',
  `limit_start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间',
  `limit_end_time` datetime NULL DEFAULT NULL COMMENT '考试结束时间',
  `paper_frame_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷结构信息表Id',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  `question_item_mess` bit(1) NULL DEFAULT NULL COMMENT '选项打乱',
  `question_mess` bit(1) NULL DEFAULT NULL COMMENT '题目打乱',
  `cheat` bit(1) NULL DEFAULT NULL COMMENT '是否防作弊',
  `exam_paper_archive_id` int NULL DEFAULT NULL COMMENT '试卷分类',
  `pass_score` int NULL DEFAULT NULL COMMENT '合格分',
  `exam_paper_build_id` bigint NULL DEFAULT NULL COMMENT '试卷构建Id',
  `credential_template_id` int NULL DEFAULT NULL COMMENT '证书模板',
  `capture` bit(1) NULL DEFAULT NULL COMMENT '考试抓拍',
  `create_department_id` int NULL DEFAULT NULL COMMENT '创建人部门',
  `watch` bit(1) NULL DEFAULT NULL COMMENT '是否允许查看答卷',
  `max_cheat_count` int NULL DEFAULT NULL COMMENT '最大作弊次数',
  `face_check` bit(1) NULL DEFAULT NULL COMMENT '人脸识别',
  `vm_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '虚拟机类别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_exam_paper
-- ----------------------------
INSERT INTO `t_exam_paper` VALUES (1, 'Shell编程课程', 1, 200, 5, 30, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '4606d338-4344-4a60-bfa3-73bd07e7c853', 1, '2023-12-13 14:30:03', b'1', b'1', b'1', b'1', 1, 120, 1, NULL, b'0', 1, b'1', 5, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (2, '综合-抽题组卷-Shell编程课程', 2, 500, 19, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'ba7e5a65-6fe3-4497-941b-b438331aa28f', 1, '2023-12-13 14:56:05', b'1', b'1', b'1', b'1', NULL, 300, 2, NULL, b'0', 1, b'1', 5, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (3, '综合-抽题组卷-Shell编程课程-修正版', 2, 500, 19, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '42e04c5d-b8e3-4be6-af19-fd9544a50de1', 1, '2023-12-13 15:18:22', b'1', b'1', b'1', b'1', 2, 300, 3, NULL, b'0', 1, b'1', 5, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (4, '测试批改试卷', 1, 90, 3, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '708d4e3f-5058-4bad-afd5-d5efae0f00ed', 1, '2023-12-13 16:11:40', b'1', b'0', b'0', b'1', 2, 60, 4, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (5, '测试批改试卷2', 1, 90, 3, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '53cb183b-65ed-4013-b6ca-dbe8053c7430', 1, '2023-12-13 16:15:35', b'1', b'0', b'0', b'1', 2, 60, 5, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (6, '测试批改试卷3333333333333333333333', 1, 90, 3, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '56e3068d-d5fb-492e-ac33-e9dc39f8ed1a', 1, '2023-12-13 16:23:32', b'1', b'0', b'0', b'1', 2, 60, 6, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (7, '测试批改试卷3333333333333333333333', 1, 90, 3, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'cbe1bc88-7fe6-47b7-999f-ccf6f459dccc', 1, '2023-12-13 17:15:46', b'1', b'0', b'0', b'1', 2, 60, 7, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (8, '测试批改试卷444', 1, 90, 3, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '8f6bfb85-2c22-4c2d-a74b-f8820ee62511', 1, '2023-12-13 17:23:30', b'1', b'0', b'0', b'1', 2, 60, 8, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (9, '测试批改试卷5', 1, 90, 3, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '64e809da-796d-482b-8a64-546b038dbc6d', 1, '2023-12-13 17:25:14', b'1', b'0', b'0', b'1', 2, 60, 9, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (10, '报名考试', 2, 300, 13, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '76c3a556-78a3-47bd-a3c7-45cf7c7208e8', 1, '2023-12-14 16:25:45', b'1', b'1', b'1', b'1', 2, 180, 10, NULL, b'0', 1, b'1', 5, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (11, '报名考试', 2, 300, 13, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '0579dc34-0067-4500-beb8-6de056dd76fc', 1, '2023-12-14 17:36:52', b'1', b'1', b'1', b'1', 2, 180, 11, NULL, b'0', 1, b'1', 5, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (12, 'Shell编程课程-期末考试', 2, 100, 5, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'a856be7c-8a42-4c61-ba1b-4e15321d2821', 1, '2023-12-18 11:36:11', b'1', b'1', b'1', b'1', 2, 40, 12, 2, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (13, 'Shell编程课程-人脸识别 考试抓拍', 1, 20, 1, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'd69e4503-5b96-4886-b706-55382cc2c6c7', 1, '2023-12-18 14:01:31', b'1', b'1', b'1', b'1', 2, 20, 13, NULL, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (14, 'Shell编程课程-人脸识别 考试抓拍', 1, 20, 1, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '318ca6a5-d3d9-455e-b78f-860d1c6974c8', 1, '2023-12-19 09:46:52', b'1', b'1', b'1', b'1', 2, 20, 14, NULL, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (15, 'Shell编程课程-人脸识别 考试抓拍', 1, 20, 1, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '0d3f5f63-10ea-483f-a051-d11c9e8eacf5', 1, '2023-12-19 11:40:44', b'1', b'1', b'1', b'1', 2, 20, 15, NULL, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (16, 'Shell编程课程-人脸识别 考试抓拍', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '4b065d5c-002a-4f3b-9e58-042d2eb58ef9', 1, '2023-12-19 11:45:38', b'1', b'1', b'1', b'1', 2, 20, 16, NULL, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (17, 'Shell编程课程-人脸识别 考试抓拍', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '9ef8d801-414b-45f7-a819-f7c96ef917af', 1, '2023-12-19 11:56:17', b'1', b'1', b'1', b'1', 2, 20, 17, NULL, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (18, 'Shell编程课程-人脸识别', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '6afd9185-dfeb-46b1-b679-6bcf2ca3147d', 1, '2023-12-19 11:58:16', b'1', b'1', b'1', b'1', 2, 20, 18, 2, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (19, 'Shell编程课程-人脸识别', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'c617a66b-afe6-4823-9c73-104c20c02f24', 1, '2023-12-26 10:00:54', b'1', b'1', b'1', b'1', 2, 20, 19, 2, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (20, 'Shell编程课程-人脸识别', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '2fe1d8a6-f23f-4638-8efc-a4e256a443dc', 1, '2023-12-26 11:00:38', b'1', b'1', b'1', b'1', 2, 20, 20, 2, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (21, 'Shell编程课程-基础测验', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'bebe4827-ad8c-4c1c-b2c9-5e7eefd50fd2', 1, '2025-01-19 16:45:03', b'1', b'1', b'1', b'1', 1, 20, 21, 2, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (22, 'Shell编程课程-综合测验', 3, 1000, 27, 150, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-01-19 16:59:02', b'1', b'1', b'1', b'1', 2, 600, 22, 2, b'1', 1, b'1', 10, b'1', NULL);
INSERT INTO `t_exam_paper` VALUES (23, 'Shell编程课程-单选测验', 2, 300, 15, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'f66991dd-e451-4a94-a74e-743a4c234537', 1, '2025-01-19 17:08:51', b'1', b'1', b'1', b'1', 1, 150, 23, NULL, b'1', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (24, 'Shell编程课程-单选测验', 2, 300, 15, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '784c00b9-c1d5-42a6-bccd-8abd30ea7033', 1, '2025-01-19 17:19:38', b'1', b'1', b'1', b'1', 1, 150, 24, NULL, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (25, 'Shell编程课程-综合测验', 3, 1000, 27, 150, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-01-19 17:19:39', b'1', b'1', b'1', b'1', 2, 600, 25, 2, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (26, 'Shell编程课程-基础测验', 1, 60, 2, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '95149a66-eb1b-4d09-90a9-e00ee75923d8', 1, '2025-01-19 17:19:40', b'1', b'1', b'1', b'1', 1, 20, 26, 2, b'0', 1, b'1', 10, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (27, 'Shell编程课程-随即组卷2025', 3, 1000, 27, 150, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-03-29 09:57:33', b'1', b'0', b'0', b'0', 2, 600, 27, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (28, '人工组卷测试', 1, 40, 2, 50, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '8d2a20a2-da20-4940-b6f8-096861be7121', 1, '2025-04-02 08:56:01', b'1', b'0', b'0', b'0', 1, 20, 28, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (31, '简答组卷', 1, 100, 2, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '52abcb3b-d0f8-4410-855f-4fd318bc2555', 1, '2025-04-02 14:24:34', b'0', b'0', b'0', b'0', 1, 50, 31, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (34, '实训测试', 4, 400, 4, 600, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '3a3e85d2-86e3-4c6b-a9a6-f240c9b07bd0', 1, '2025-04-08 11:34:26', b'1', b'0', b'0', b'0', 1, 200, 34, NULL, b'0', 1, b'1', NULL, b'0', 'test');
INSERT INTO `t_exam_paper` VALUES (35, '实训测试二', 4, 400, 4, 600, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '21776e27-3ac3-4206-a225-3908d285bfd1', 1, '2025-04-09 10:38:21', b'1', b'0', b'0', b'0', 1, 200, 35, NULL, b'0', 1, b'1', NULL, b'0', 'test');
INSERT INTO `t_exam_paper` VALUES (36, 'KYCA（桌面）模拟题', 1, 1000, 10, 120, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '4f3e15b4-f3c0-429f-aef3-15641198193c', 1, '2025-04-28 17:28:23', b'1', b'0', b'0', b'0', 1, 600, 36, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (37, '软件工程基础题01', 1, 181, 9, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'ec0817a5-f3de-4d7a-bf6e-a92b0d705fef', 1, '2025-04-30 10:51:12', b'1', b'0', b'0', b'0', 1, 100, 38, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (38, '软件工程基础题01', 1, 131, 8, 2, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'f4946ee1-37ac-403d-bb25-05378f20237d', 1, '2025-04-30 11:12:49', b'1', b'0', b'1', b'0', 1, 80, 37, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (39, '软件', 2, 150, 6, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'd2be70ba-eb26-4483-a84b-d451cc2c33e0', 1, '2025-04-30 11:32:57', b'1', b'1', b'1', b'1', 2, 90, 39, 1, b'1', 1, b'1', 1, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (40, '软件01', 2, 150, 6, 3, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'a4b32852-0fa6-47dd-9b7a-d6234efe9a55', 1, '2025-04-30 11:33:36', b'1', b'1', b'1', b'1', 2, 100, 40, 1, b'1', 1, b'1', 1, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (41, '软件-随机', 3, 40, 2, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-04-30 11:54:27', b'1', b'0', b'0', b'0', NULL, 30, 41, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (42, '软件-随机01', 3, 40, 2, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-04-30 15:25:28', b'1', b'0', b'0', b'0', NULL, 30, 42, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (43, '测试01', 2, 21, 2, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '6aac1a36-c0ce-4e15-8957-0825287d5b3c', 1, '2025-04-30 15:32:44', b'1', b'0', b'0', b'0', 1, 20, 43, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (44, '实训测试01', 4, 40, 1, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '54487549-b6e5-47d2-968d-2b6c9b943bd0', 1, '2025-04-30 15:38:47', b'1', b'0', b'0', b'0', 1, 30, 44, 2, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (45, '人工', 1, 81, 5, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '38411f28-a1e1-4c42-b289-23458cd8b76e', 1, '2025-04-30 15:55:03', b'1', b'0', b'0', b'0', 1, 55, 45, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (46, '测试06', 3, 20, 2, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-05-06 09:59:26', b'1', b'0', b'0', b'0', NULL, 10, 46, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (47, '软件工程第一次练习', 1, 275, 10, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'b55b58b8-0927-4461-b205-420f90b83027', 1, '2025-05-07 08:56:15', b'1', b'0', b'0', b'0', NULL, 200, 49, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (48, '软件工程第二次练习', 1, 275, 10, 9, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '641cfd45-52b9-41d9-b861-f232655a4b61', 1, '2025-05-07 09:02:53', b'1', b'0', b'0', b'0', 2, 210, 50, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (49, '信息安全第二次练习', 2, 90, 4, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '7398ff47-e20e-46be-9c25-a9c8ea71d6aa', 1, '2025-05-07 09:16:51', b'1', b'0', b'0', b'0', 2, 60, 51, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (50, '信息安全第3次练习', 2, 90, 4, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '82dac7bb-d7d6-4fd0-bfdb-3707269561e5', 1, '2025-05-07 14:14:16', b'1', b'0', b'0', b'0', 2, 60, 52, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (51, '信息安全第4次练习', 2, 90, 4, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '5512b0e3-9f4f-4bcd-bc63-1fb619ae5953', 1, '2025-05-07 14:14:18', b'1', b'0', b'0', b'0', 2, 60, 53, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (52, '测试实训考试', 4, 40, 1, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '7383ef18-c168-4a53-8afa-f932458e605f', 1, '2025-05-07 14:51:04', b'1', b'0', b'0', b'0', 1, 40, 54, 1, b'0', 1, b'1', NULL, b'0', 'kylin-desktop');
INSERT INTO `t_exam_paper` VALUES (53, '测试基础实训', 4, 10, 1, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'be566736-a070-4a86-b014-dfcd88c2152f', 1, '2025-05-07 17:05:59', b'1', b'0', b'0', b'0', 1, 10, 55, NULL, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (54, 'desktop试卷', 4, 100, 1, 3, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '8f9cce8e-abe7-44ff-8e75-c36a23a3061f', 1, '2025-05-07 17:09:50', b'1', b'0', b'0', b'0', 3, 60, 56, NULL, b'0', 1, b'1', NULL, b'0', 'kylin-desktop');
INSERT INTO `t_exam_paper` VALUES (55, '软件工程测试', 3, 20, 2, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-05-08 10:56:11', b'1', b'0', b'0', b'0', NULL, 10, 48, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (56, '软件技术第一次考试', 1, 95, 3, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'e8e6b8af-18c6-406a-8dbe-a20275cd145d', 1, '2025-05-09 09:27:48', b'1', b'0', b'0', b'0', 1, 60, 57, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (57, '软件第2测试', 1, 70, 2, 2, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'a9a871d9-8123-4494-89a1-47d533836262', 1, '2025-05-09 11:51:33', b'1', b'0', b'0', b'0', 1, 50, 58, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (58, '1', 3, 20, 2, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', NULL, 1, '2025-05-11 11:57:01', b'1', b'0', b'0', b'0', NULL, 10, 59, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (59, '11cs1', 4, 10, 1, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '9b390991-0509-45ed-9084-3d9735aac00a', 1, '2025-05-14 10:47:34', b'1', b'0', b'0', b'0', 1, 10, 60, 1, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (60, '实训111', 4, 100, 1, 20, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '8960e702-db4d-4a31-b3f6-71df754a6b42', 1, '2025-05-14 10:48:46', b'1', b'0', b'0', b'0', 1, 100, 61, 2, b'0', 1, b'1', NULL, b'0', 'kylin-desktop');
INSERT INTO `t_exam_paper` VALUES (61, '第1次考试', 2, 90, 4, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '82438bc7-6e0b-49d4-9fdb-c91cf8c1dafa', 1, '2025-05-31 15:27:51', b'1', b'0', b'0', b'0', 2, 60, 62, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (62, '第1次考试', 2, 90, 4, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'b3e81159-8d0b-4994-a3ec-ece03335ce5c', 1, '2025-05-31 15:33:06', b'1', b'0', b'0', b'0', 2, 60, 63, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (63, '人工', 1, 40, 2, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'f28d97ee-3e3a-49b8-8c99-b337471567fb', 1, '2025-06-18 08:39:12', b'1', b'0', b'0', b'0', NULL, 20, 65, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (64, '服务器', 4, 90, 1, 30, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '9da59c22-0f30-45c4-adbb-92e1b7b2a85e', 1, '2025-06-28 14:52:33', b'1', b'0', b'0', b'0', 1, 60, 66, 1, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (65, '桌面', 4, 50, 1, 100, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '27f77592-fb92-4f9c-a055-e5e071d828ca', 1, '2025-06-28 16:28:43', b'1', b'0', b'0', b'0', 1, 30, 67, NULL, b'0', 1, b'1', NULL, b'0', 'kylin-desktop');
INSERT INTO `t_exam_paper` VALUES (66, '基本配置', 4, 50, 1, 60, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '37cc3ebc-ceec-4977-9fe9-d5d907d7d3c6', 1, '2025-07-02 16:22:27', b'0', b'0', b'0', b'0', 1, 50, 68, NULL, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (67, 'IP配置', 4, 50, 1, 400, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'ae3f8945-7363-4b1f-8c7e-b433c48810a6', 1, '2025-07-03 09:34:29', b'0', b'0', b'0', b'0', 1, 50, 69, 1, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (68, '测试', 4, 10, 1, 400, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'db645f28-1448-436a-b888-061bc461c55b', 1, '2025-07-03 11:22:46', b'0', b'0', b'0', b'0', 1, 10, 70, NULL, b'0', 1, b'1', NULL, b'0', 'kylin-desktop');
INSERT INTO `t_exam_paper` VALUES (69, '信创测试1', 4, 5, 1, 200, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '6c2dbd1d-6dd7-4056-8935-5c6ae5c538aa', 1, '2025-07-20 10:56:43', b'0', b'0', b'0', b'0', 6, 5, 71, NULL, b'0', 1, b'1', NULL, b'0', 'kylin-server');
INSERT INTO `t_exam_paper` VALUES (70, '信息安全第二次练习', 2, 90, 4, 1, '2025-09-13 00:00:00', '2025-09-15 00:00:00', 'cd5b15ca-83cf-4249-828b-dd62b076c0ad', 1, '2025-08-21 15:27:24', b'0', b'1', b'1', b'1', 2, 60, 73, 2, b'1', 1, b'1', 3, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (71, '1', 1, 10, 1, 5, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '79bfe66a-8bd0-4cfc-9a94-c21b396ebbe1', 1, '2025-08-22 08:16:04', b'1', b'0', b'0', b'0', 1, 10, 74, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (72, '12', 1, 50, 1, 10, '2025-09-13 00:00:00', '2025-09-15 00:00:00', '71f5674b-38c5-4637-80e0-cdc8d74bcc91', 1, '2025-09-04 09:17:42', b'0', b'0', b'0', b'0', 2, 50, 75, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (73, '基础学习一期考查', 2, 320, 10, 90, '2025-10-22 00:00:00', '2025-10-26 00:00:00', '01a54a56-8721-4606-893f-58199ebdedca', 1, '2025-10-22 14:50:50', b'0', b'0', b'0', b'0', 1, 320, 77, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (74, '计算机网络一期培训检验', 2, 500, 10, 90, '2025-10-22 00:00:00', '2025-10-24 00:00:00', '7f80b173-612a-48d6-85ee-c963e70057de', 1, '2025-10-22 15:21:06', b'0', b'0', b'0', b'0', 2, 500, 78, 2, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (75, '计算器网络一期考试', 2, 1000, 20, 90, '2025-10-22 00:00:00', '2025-10-25 00:00:00', 'dd40acee-97c3-45b6-af0f-29a166ccd2f6', 1, '2025-10-22 17:17:10', b'0', b'0', b'1', b'0', 1, 600, 79, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (76, '计算机网络基础答题', 2, 1000, 10, 90, '2025-10-22 00:00:00', '2025-10-31 00:00:00', '917f3201-4543-45b8-bd08-6c5f1902d1ca', 1, '2025-10-22 19:01:18', b'0', b'0', b'0', b'0', 1, 600, 80, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (77, '测试考试', 3, 400, 20, 300, '2025-11-20 00:00:00', '2025-11-21 00:00:00', NULL, 1, '2025-11-14 20:21:35', b'0', b'0', b'0', b'0', 2, 390, 81, NULL, b'1', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (78, '张三考试', 1, 820, 5, 20, '2025-11-14 00:00:00', '2025-11-16 00:00:00', '355602bd-0b18-45aa-a0c1-cd376c65390f', 1, '2025-11-14 20:34:01', b'0', b'0', b'0', b'0', 1, 500, 82, 1, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (79, '测试', 1, 200, 1, 20, '2025-11-18 00:00:00', '2025-11-23 00:00:00', 'f37d3856-05f0-4c75-a4d6-f2c789fa409b', 1, '2025-11-18 11:41:14', b'0', b'0', b'0', b'0', NULL, 100, 83, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (80, '测试', 1, 200, 1, 20, '2025-11-18 00:00:00', '2025-11-23 00:00:00', 'ab23ad1f-4873-413f-978f-0be3c8a1b46c', 1, '2025-11-18 11:44:30', b'0', b'0', b'0', b'0', NULL, 100, 84, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (81, '测试附件', 1, 1000, 1, 60, '2025-11-27 00:00:00', '2025-11-28 00:00:00', 'bd7dffcc-634a-47b7-a5c6-dd77a88bc9a0', 1, '2025-11-27 17:50:41', b'1', b'0', b'0', b'0', NULL, 1000, 85, NULL, b'0', 1, b'1', NULL, b'0', NULL);
INSERT INTO `t_exam_paper` VALUES (82, '测试附件', 1, 1000, 1, 60, '2025-11-27 00:00:00', '2025-11-28 00:00:00', '9a94bafd-5e92-4cb9-a8d9-b66576061960', 1, '2025-11-27 17:52:53', b'0', b'0', b'0', b'0', NULL, 1000, 86, NULL, b'0', 1, b'1', NULL, b'0', NULL);

-- ----------------------------
-- Table structure for t_exam_paper_answer
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_paper_answer`;
CREATE TABLE `t_exam_paper_answer`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exam_paper_id` bigint NULL DEFAULT NULL COMMENT '试卷Id',
  `paper_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `paper_type` int NULL DEFAULT NULL COMMENT '试卷类型(人工组卷、抽题组卷、随机组卷)',
  `system_score` int NULL DEFAULT NULL COMMENT '系统判分',
  `user_score` int NULL DEFAULT NULL COMMENT '用户得分',
  `paper_score` int NULL DEFAULT NULL COMMENT '试卷总分',
  `question_correct` int NULL DEFAULT NULL COMMENT '正确题数',
  `question_count` int NULL DEFAULT NULL COMMENT '题目总数',
  `do_time` int NULL DEFAULT NULL COMMENT '耗时',
  `status` int NULL DEFAULT NULL COMMENT '答卷状态',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `judge_user` int NULL DEFAULT NULL COMMENT '批改人',
  `answer_frame_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '答卷内容Id',
  `exam_paper_archive_id` int NULL DEFAULT NULL COMMENT '试卷分类Id',
  `passed` bit(1) NULL DEFAULT NULL COMMENT '是否合格',
  `pass_score` int NULL DEFAULT NULL COMMENT '合格分',
  `exam_paper_build_id` bigint NULL DEFAULT NULL COMMENT '试卷构建Id',
  `credential_template_id` int NULL DEFAULT NULL COMMENT '证书模板',
  `limit_start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间',
  `limit_end_time` datetime NULL DEFAULT NULL COMMENT '考试结束时间',
  `create_department_id` int NULL DEFAULT NULL COMMENT '创建人部门',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  `watch` bit(1) NULL DEFAULT NULL COMMENT '是否允许查看答卷',
  `preview_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '答卷导出',
  `exam_paper_child_id` bigint NULL DEFAULT NULL COMMENT '子试卷id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 165 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_exam_paper_answer
-- ----------------------------
INSERT INTO `t_exam_paper_answer` VALUES (1, 1, 'Shell编程课程', 1, 40, 40, 200, 0, 5, 192, 2, 2, '2023-12-13 14:33:30', NULL, 'd89ca73a-3a24-4ff8-8360-3884a31f021f', 1, b'0', 120, 1, NULL, '2023-12-13 00:00:00', '2023-12-15 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (2, 2, '综合-抽题组卷-Shell编程课程', 2, 240, 100, 500, 1, 19, 256, 2, 2, '2023-12-13 15:02:27', 1, '0bdde68a-f7c1-4435-820a-1ef0ec5decd2', NULL, b'0', 300, 2, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (3, 3, '综合-抽题组卷-Shell编程课程-修正版', 2, 250, 280, 500, 9, 19, 106, 2, 2, '2023-12-13 15:20:14', 1, '847e8a6f-e67f-4a2f-a385-76984e3c586a', 2, b'0', 300, 3, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (4, 4, '测试批改试卷', 1, 0, 10, 90, 0, 3, 28, 2, 2, '2023-12-13 16:12:14', 1, '93e831c2-1e1a-4fd2-b284-e35ba8597e5e', 2, b'0', 60, 4, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (5, 5, '测试批改试卷2', 1, 0, 20, 90, 0, 3, 14, 2, 2, '2023-12-13 16:15:53', 1, 'd7831c9c-0281-4b7a-8853-e77a6a90088b', 2, b'0', 60, 5, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (6, 6, '测试批改试卷3333333333333333333333', 1, 0, 20, 90, 0, 3, 16, 2, 2, '2023-12-13 16:23:57', 1, '8de46e6f-9d43-4e63-af63-8137526dc2f9', 2, b'0', 60, 6, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (7, 7, '测试批改试卷3333333333333333333333', 1, 0, 20, 90, 0, 3, 7, 2, 2, '2023-12-13 17:15:55', 1, 'ef511109-48e8-4bef-835a-f3a426becaf6', 2, b'0', 60, 7, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (8, 8, '测试批改试卷444', 1, 0, 30, 90, 0, 3, 5, 2, 2, '2023-12-13 17:24:07', 1, 'dabe16fc-1929-436f-9f4f-16ef72d7d6be', 2, b'0', 60, 8, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (9, 9, '测试批改试卷5', 1, 0, 20, 90, 0, 3, 7, 2, 2, '2023-12-13 17:25:27', 1, 'a143ae06-771a-40d8-b488-2d6cf512672f', 2, b'0', 60, 9, NULL, '2023-12-13 00:00:00', '2023-12-14 00:00:00', 2, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (10, 11, '报名考试', 2, 0, 0, 300, 0, 13, 280, 2, 2, '2023-12-14 17:41:50', NULL, 'eb0d1340-bead-408d-86bc-1dc73b07f18d', 2, b'0', 180, 11, NULL, '2023-12-14 00:00:00', '2023-12-16 00:00:00', 6, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (11, 11, '报名考试', 2, 160, 160, 300, 6, 13, 393, 2, 4, '2023-12-14 17:51:39', NULL, 'cfed0ff1-c23f-48db-8117-590c3362ec83', 2, b'0', 180, 11, NULL, '2023-12-14 00:00:00', '2023-12-16 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (12, 10, '报名考试', 2, 60, 60, 300, 2, 13, 91, 2, 2, '2023-12-18 10:36:37', NULL, '07d8b862-dc09-4584-859c-3e2dbf1ef084', 2, b'0', 180, 10, NULL, '2023-12-16 00:00:00', '2023-12-31 00:00:00', 6, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (13, 10, '报名考试', 2, 0, 0, 300, 0, 13, 9, 2, 4, '2023-12-18 15:04:34', NULL, 'd5e4aea4-19ef-4322-9eef-1032c28031bf', 2, b'0', 180, 10, NULL, '2023-12-16 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (14, 12, 'Shell编程课程-期末考试', 2, 60, 60, 100, 3, 5, 71, 2, 5, '2023-12-18 15:12:59', NULL, '2233cade-1222-4419-b9fe-69e269e70daa', 2, b'1', 40, 12, 2, '2023-12-18 00:00:00', '2023-12-31 00:00:00', 6, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (15, 14, 'Shell编程课程-人脸识别 考试抓拍', 1, 0, 0, 20, 0, 1, 453, 2, 4, '2023-12-19 11:38:35', NULL, '3a6e824c-5159-4935-b6c9-32ae27a2cc9f', 2, b'0', 20, 14, NULL, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (16, 15, 'Shell编程课程-人脸识别 考试抓拍', 1, 0, 0, 20, 0, 1, 60, 2, 4, '2023-12-19 11:42:42', NULL, '302b2610-7d70-4be9-9aa8-2e966402018c', 2, b'0', 20, 15, NULL, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (17, 16, 'Shell编程课程-人脸识别 考试抓拍', 1, 30, 30, 60, 0, 2, 150, 2, 4, '2023-12-19 11:48:57', NULL, '3dc7fb90-4c07-4d3f-8e5f-1d35559a4b50', 2, b'1', 20, 16, NULL, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (18, 17, 'Shell编程课程-人脸识别 考试抓拍', 1, 40, 40, 60, 1, 2, 72, 2, 4, '2023-12-19 11:57:56', NULL, 'a91aaf66-ac8e-41f1-8045-32e7ea06726c', 2, b'1', 20, 17, NULL, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (19, 18, 'Shell编程课程-人脸识别', 1, 40, 40, 60, 1, 2, 24, 2, 4, '2023-12-19 11:58:50', NULL, 'a2e5a315-f39c-47b5-b4ef-fdae98bf3576', 2, b'1', 20, 18, 2, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (20, 19, 'Shell编程课程-人脸识别', 1, 50, 50, 60, 1, 2, 127, 2, 4, '2023-12-26 10:59:09', NULL, 'b2651187-75b7-418d-8f5c-c515f0378380', 2, b'1', 20, 19, 2, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (21, 20, 'Shell编程课程-人脸识别', 1, 60, 60, 60, 2, 2, 94, 2, 4, '2023-12-26 11:03:05', NULL, 'b63c189a-5462-41fc-840a-7590e280e3fe', 2, b'1', 20, 20, 2, '2023-12-19 00:00:00', '2023-12-31 00:00:00', 7, b'0', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (22, 26, 'Shell编程课程-基础测验', 1, 10, 10, 60, 0, 2, 68, 2, 4, '2025-01-19 17:20:51', NULL, '68cb66a5-3ce4-471e-afdb-acf47471c8f5', 1, b'0', 20, 26, 2, '2025-01-14 00:00:00', '2025-02-14 00:00:00', 7, b'1', b'1', NULL, NULL);
INSERT INTO `t_exam_paper_answer` VALUES (23, 26, 'Shell编程课程-基础测验', 1, 50, 50, 60, 1, 2, 24, 2, 4, '2025-01-19 17:45:01', NULL, 'e92405f5-7566-41d3-93d8-ce2b1a4091bc', 1, b'1', 20, 26, 2, '2025-01-14 00:00:00', '2025-02-14 00:00:00', 7, b'0', b'1', NULL, NULL);
