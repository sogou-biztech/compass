/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan01` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan01`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0101`;

CREATE TABLE `plan_0101` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0102`;

CREATE TABLE `plan_0102` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0103`;

CREATE TABLE `plan_0103` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0104`;

CREATE TABLE `plan_0104` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0105`;

CREATE TABLE `plan_0105` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0106`;

CREATE TABLE `plan_0106` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0107`;

CREATE TABLE `plan_0107` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0108`;

CREATE TABLE `plan_0108` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan02` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan02`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0201`;

CREATE TABLE `plan_0201` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0202`;

CREATE TABLE `plan_0202` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0203`;

CREATE TABLE `plan_0203` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0204`;

CREATE TABLE `plan_0204` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0205`;

CREATE TABLE `plan_0205` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0206`;

CREATE TABLE `plan_0206` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0207`;

CREATE TABLE `plan_0207` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0208`;

CREATE TABLE `plan_0208` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan03` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan03`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0301`;

CREATE TABLE `plan_0301` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0302`;

CREATE TABLE `plan_0302` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0303`;

CREATE TABLE `plan_0303` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0304`;

CREATE TABLE `plan_0304` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0305`;

CREATE TABLE `plan_0305` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0306`;

CREATE TABLE `plan_0306` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0307`;

CREATE TABLE `plan_0307` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0308`;

CREATE TABLE `plan_0308` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan04` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan04`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0401`;

CREATE TABLE `plan_0401` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0402`;

CREATE TABLE `plan_0402` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0403`;

CREATE TABLE `plan_0403` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0404`;

CREATE TABLE `plan_0404` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0405`;

CREATE TABLE `plan_0405` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0406`;

CREATE TABLE `plan_0406` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0407`;

CREATE TABLE `plan_0407` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0408`;

CREATE TABLE `plan_0408` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan05` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan05`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0501`;

CREATE TABLE `plan_0501` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0502`;

CREATE TABLE `plan_0502` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0503`;

CREATE TABLE `plan_0503` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0504`;

CREATE TABLE `plan_0504` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0505`;

CREATE TABLE `plan_0505` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0506`;

CREATE TABLE `plan_0506` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0507`;

CREATE TABLE `plan_0507` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0508`;

CREATE TABLE `plan_0508` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;





/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan06` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan06`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0601`;

CREATE TABLE `plan_0601` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0602`;

CREATE TABLE `plan_0602` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0603`;

CREATE TABLE `plan_0603` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0604`;

CREATE TABLE `plan_0604` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0605`;

CREATE TABLE `plan_0605` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0606`;

CREATE TABLE `plan_0606` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0607`;

CREATE TABLE `plan_0607` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0608`;

CREATE TABLE `plan_0608` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan07` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan07`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0701`;

CREATE TABLE `plan_0701` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0702`;

CREATE TABLE `plan_0702` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0703`;

CREATE TABLE `plan_0703` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0704`;

CREATE TABLE `plan_0704` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0705`;

CREATE TABLE `plan_0705` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0706`;

CREATE TABLE `plan_0706` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0707`;

CREATE TABLE `plan_0707` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0708`;

CREATE TABLE `plan_0708` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.0.67-community-nt : Database - plan01
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plan08` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `plan08`;

/*Table structure for table `plan` */

DROP TABLE IF EXISTS `plan_0801`;

CREATE TABLE `plan_0801` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `plan_0802`;

CREATE TABLE `plan_0802` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0803`;

CREATE TABLE `plan_0803` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0804`;

CREATE TABLE `plan_0804` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0805`;

CREATE TABLE `plan_0805` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0806`;

CREATE TABLE `plan_0806` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0807`;

CREATE TABLE `plan_0807` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `plan_0808`;

CREATE TABLE `plan_0808` (
  `planid` bigint(10) NOT NULL auto_increment,
  `accountid` bigint(10) default NULL,
  `name` varchar(40) default NULL,
  `createdate` date default NULL,
  PRIMARY KEY  (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



