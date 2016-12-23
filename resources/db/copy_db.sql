/**
 * SQL script to copy one e-TBM database to another. Useful when there is change in the model
 * and you want to preserve the data
 */
use etbm3_2;




INSERT INTO `etbm3`.`workspace`
(`id`,
`name`,
`patientNameComposition`,
`confirmedCaseNumber`,
`sendSystemMessages`,
`monthsToAlertExpiredMedicines`,
`caseValidationTB`,
`caseValidationDRTB`,
`caseValidationNTM`,
`minStockOnHand`,
`maxStockOnHand`,
`suspectCaseNumber`,
`customId`)
select
`id`,
`name`,
`patientNameComposition`,
`confirmedCaseNumber`,
`sendSystemMessages`,
`monthsToAlertExpiredMedicines`,
`caseValidationTB`,
`caseValidationDRTB`,
`caseValidationNTM`,
`minStockOnHand`,
`maxStockOnHand`,
`suspectCaseNumber`,
`customId`
from workspace;



INSERT INTO `etbm3`.`sys_user`
(`id`,
`comments`,
`email`,
`login`,
`name`,
`password`,
`registrationDate`,
`active`,
`passwordExpired`,
`emailConfirmed`,
`timeZone`,
`PARENTUSER_ID`,
`customId`,
`sendSystemMessages`,
`ulaAccepted`,
`passwordResetToken`,
`mobile`,
`language`)
select
`id`,
`comments`,
`email`,
`login`,
`name`,
`password`,
`registrationDate`,
`active`,
`passwordExpired`,
`emailConfirmed`,
`timeZone`,
`PARENTUSER_ID`,
`customId`,
`sendSystemMessages`,
`ulaAccepted`,
`passwordResetToken`,
`mobile`,
`language`
from sys_user;




INSERT INTO `etbm3`.`workspacelog`
(`id`,
`name`,
`unit_id`)
select
`id`,
`name`,
`unit_id`
from workspacelog;



INSERT INTO `etbm3`.`workspaceview`
(`id`,
`logoImage`,
`picture`,
`pictureContentType`)
select
`id`,
`logoImage`,
`picture`,
`pictureContentType`
from workspaceview;


INSERT INTO `etbm3`.`countrystructure`
(`id`,
`STRUCTURE_LEVEL`,
`name`,
`WORKSPACE_ID`)
select
`id`,
`STRUCTURE_LEVEL`,
`name`,
`WORKSPACE_ID`
from countrystructure;


INSERT INTO `etbm3`.`administrativeunit`
(`id`,
`customId`,
`name`,
`unitsCount`,
`WORKSPACE_ID`,
`COUNTRYSTRUCTURE_ID`,
`pid0`,
`pid1`,
`pid2`,
`pid3`,
`pname0`,
`pname1`,
`pname2`,
`pname3`)
select
`id`,
`customId`,
`name`,
`unitsCount`,
`WORKSPACE_ID`,
`COUNTRYSTRUCTURE_ID`,
`pid0`,
`pid1`,
`pid2`,
`pid3`,
`pname0`,
`pname1`,
`pname2`,
`pname3`
from administrativeunit;



INSERT INTO `etbm3`.`unit`
(`id`,
`discriminator`,
`name`,
`shortName`,
`active`,
`address`,
`addressCompl`,
`zipCode`,
`customId`,
`WORKSPACE_ID`,
`ADMINUNIT_ID`,
`shipAddress`,
`shipAddressCompl`,
`shipContactName`,
`shipContactPhone`,
`shipZipCode`,
`ship_adminunit_id`,
`tbFacility`,
`drtbFacility`,
`ntmFacility`,
`numDaysOrder`,
`receiveFromManufacturer`,
`notificationUnit`,
`inventoryCloseDate`,
`inventoryStartDate`,
`performMicroscopy`,
`performCulture`,
`performDst`,
`performXpert`)
select
`id`,
`discriminator`,
`name`,
`shortName`,
`active`,
`address`,
`addressCompl`,
`zipCode`,
`customId`,
`WORKSPACE_ID`,
`ADMINUNIT_ID`,
`shipAddress`,
`shipAddressCompl`,
`shipContactName`,
`shipContactPhone`,
`shipZipCode`,
`ship_adminunit_id`,
`tbFacility`,
`drtbFacility`,
`ntmFacility`,
`numDaysOrder`,
`receiveFromManufacturer`,
`notificationUnit`,
`inventoryCloseDate`,
`inventoryStartDate`,
`performMicroscopy`,
`performCulture`,
`performDst`,
`performXpert`
from unit;


update etbm3.unit u1
join unit u2 on u2.id=u1.id
set u1.authorizerunit_id=u2.authorizerunit_id,
u1.supplier_id=u2.supplier_id;



INSERT INTO `etbm3`.`source`
(`id`,
`shortName`,
`name`,
`WORKSPACE_ID`,
`customId`,
`active`)
select
`id`,
`shortName`,
`name`,
`WORKSPACE_ID`,
`customId`,
`active`
from source;



INSERT INTO `etbm3`.`substance`
(`id`,
`shortName`,
`line`,
`name`,
`WORKSPACE_ID`,
`customId`,
`active`)
select
`id`,
`shortName`,
`line`,
`name`,
`WORKSPACE_ID`,
`customId`,
`active`
from substance;




INSERT INTO `etbm3`.`agerange`
(`id`,
`iniAge`,
`WORKSPACE_ID`,
`endAge`)
select
`id`,
`iniAge`,
`WORKSPACE_ID`,
`endAge`
from agerange;

INSERT INTO `etbm3`.`batch`
(`id`,
`batchNumber`,
`brandName`,
`expiryDate`,
`manufacturer`,
`unitPrice`,
`PRODUCT_ID`,
`WORKSPACE_ID`,
`MEDICINE_ID`)
select `id`,
`batchNumber`,
`brandName`,
`expiryDate`,
`manufacturer`,
`unitPrice`,
`PRODUCT_ID`,
`WORKSPACE_ID`,
`MEDICINE_ID`
from batch;


INSERT INTO `etbm3`.`batchdispensing`
(`id`,
`quantity`,
`BATCH_ID`,
`DISPENSING_ID`,
`SOURCE_ID`)
select
`id`,
`quantity`,
`BATCH_ID`,
`DISPENSING_ID`,
`SOURCE_ID`
from batchdispensing;


INSERT INTO `batchmovement`
(`id`,
`quantity`,
`BATCH_ID`,
`MOVEMENT_ID`,
`availableQuantity`,
`header`)
select
`id`,
`quantity`,
`BATCH_ID`,
`MOVEMENT_ID`,
`availableQuantity`,
`header`
from batchmovement;



INSERT INTO `etbm3`.`userlog`
(`id`,
`name`)
select
`id`,
`name`
from userlog;




INSERT INTO `etbm3`.`commandhistory`
(`id`,
`type`,
`action`,
`entityId`,
`entityName`,
`parentId`,
`data`,
`dataType`,
`execDate`,
`userlog_id`,
`workspacelog_id`,
`unit_id`)
select
`id`,
`type`,
`action`,
`entityId`,
`entityName`,
`parentId`,
`data`,
`dataType`,
`execDate`,
`userlog_id`,
`workspacelog_id`,
`unit_id`
from commandhistory;


INSERT INTO `etbm3`.`errorlog`
(`id`,
`exceptionClass`,
`exceptionMessage`,
`stackTrace`,
`url`,
`userName`,
`userId`,
`errorDate`,
`workspace`,
`request`)
select
`id`,
`exceptionClass`,
`exceptionMessage`,
`stackTrace`,
`url`,
`userName`,
`userId`,
`errorDate`,
`workspace`,
`request`
from errorlog;


INSERT INTO `etbm3`.`product`
(`id`,
`name`,
`shortName`,
`category`,
`line`,
`WORKSPACE_ID`,
`customId`,
`discriminator`,
`active`)
select
`id`,
`name`,
`shortName`,
`category`,
`line`,
`WORKSPACE_ID`,
`customId`,
`discriminator`,
`active`
from product;


INSERT INTO `etbm3`.`medicine_substances`
(`medicine_id`,
`substance_id`)
select
`medicine_id`,
`substance_id`
from medicine_substances;



INSERT INTO `etbm3`.`patient`
(`id`,
`birthDate`,
`gender`,
`name`,
`lastName`,
`middleName`,
`customId`,
`motherName`,
`WORKSPACE_ID`)
select
`id`,
`birthDate`,
`gender`,
`name`,
`lastName`,
`middleName`,
`customId`,
`motherName`,
`WORKSPACE_ID`
from patient;



INSERT INTO `etbm3`.`regimen`
(`id`,
`name`,
`WORKSPACE_ID`,
`customId`,
`classification`,
`active`)
select
`id`,
`name`,
`WORKSPACE_ID`,
`customId`,
`classification`,
`active`
from regimen;



INSERT INTO `etbm3`.`report`
(`id`,
`title`,
`published`,
`registrationDate`,
`owner_id`,
`dashboard`,
`data`,
`WORKSPACE_ID`)
select
`id`,
`title`,
`published`,
`registrationDate`,
`owner_id`,
`dashboard`,
`data`,
`WORKSPACE_ID`
from report;


INSERT INTO `etbm3`.`searchable`
(`id`,
`title`,
`subtitle`,
`type`,
`unit_id`,
`workspace_id`)
select
`id`,
`title`,
`subtitle`,
`type`,
`unit_id`,
`workspace_id`
from searchable;


INSERT INTO `etbm3`.`userprofile`
(`id`,
`name`,
`WORKSPACE_ID`,
`customId`)
select
`id`,
`name`,
`WORKSPACE_ID`,
`customId`
from userprofile;



INSERT INTO `etbm3`.`userworkspace`
(`id`,
`playOtherUnits`,
`USER_VIEW`,
`ADMINUNIT_ID`,
`UNIT_ID`,
`USER_ID`,
`WORKSPACE_ID`,
`administrator`)
select
`id`,
`playOtherUnits`,
`USER_VIEW`,
`ADMINUNIT_ID`,
`UNIT_ID`,
`USER_ID`,
`WORKSPACE_ID`,
`administrator`
from userworkspace;



INSERT INTO `etbm3`.`systemconfig`
(`id`,
`allowRegPage`,
`systemURL`,
`UNIT_ID`,
`USERPROFILE_ID`,
`WORKSPACE_ID`,
`adminMail`,
`otherlinks`,
`updateSite`,
`pubds_workspace_id`,
`ulaActive`,
clientMode)
select
`id`,
`allowRegPage`,
`systemURL`,
`UNIT_ID`,
`USERPROFILE_ID`,
`WORKSPACE_ID`,
`adminMail`,
`otherlinks`,
`updateSite`,
`pubds_workspace_id`,
`ulaActive`,
false
from systemconfig;



INSERT INTO `etbm3`.`tag`
(`id`,
`name`,
`WORKSPACE_ID`,
`sqlCondition`,
`consistencyCheck`,
`active`,
`dailyUpdate`)
select
`id`,
`name`,
`WORKSPACE_ID`,
`sqlCondition`,
`consistencyCheck`,
`active`,
`dailyUpdate`
from tag;



INSERT INTO `etbm3`.`tbcase`
(`id`,
`age`,
`classification`,
`state`,
`outcome`,
`otherOutcome`,
`transferring`,
`registrationNumber`,
`registrationDate`,
`caseNumber`,
`diagnosisDate`,
`CURR_ADDRESS`,
`CURR_COMPLEMENT`,
`CURR_ZIPCODE`,
`NOTIF_ADDRESS`,
`NOTIF_COMPLEMENT`,
`NOTIF_LOCALITYTYPE`,
`NOTIF_ZIPCODE`,
`CURR_ADMINUNIT_ID`,
`NOTIF_ADMINUNIT_ID`,
`MOBILENUMBER`,
`PHONENUMBER`,
`endTreatmentDate`,
`iniTreatmentDate`,
`infectionSite`,
`caseDefinition`,
`customId`,
`nationality`,
`outcomeDate`,
`registrationGroup`,
`registrationGroupOther`,
`diagnosisType`,
`PATIENT_ID`,
`NOTIFICATION_UNIT_ID`,
`drugResistanceType`,
`patientContactName`,
`WORKSPACE_ID`,
`OWNER_UNIT_ID`,
`TRANSFER_OUT_UNIT_ID`,
`REGIMEN_ID`,
`movedToIndividualized`,
`extrapulmonaryType`,
`extrapulmonaryType2`,
`pulmonaryType`,
`validated`,
`suspectClassification`,
`secDrugsReceived`,
`lastBmuDateTbRegister`,
`lastBmuTbRegistNumber`,
`movedSecondLineTreatment`)
select
`id`,
`age`,
`classification`,
`state`,
`outcome`,
`otherOutcome`,
`transferring`,
`registrationNumber`,
`registrationDate`,
`caseNumber`,
`diagnosisDate`,
`CURR_ADDRESS`,
`CURR_COMPLEMENT`,
`CURR_ZIPCODE`,
`NOTIF_ADDRESS`,
`NOTIF_COMPLEMENT`,
`NOTIF_LOCALITYTYPE`,
`NOTIF_ZIPCODE`,
`CURR_ADMINUNIT_ID`,
`NOTIF_ADMINUNIT_ID`,
`MOBILENUMBER`,
`PHONENUMBER`,
`endTreatmentDate`,
`iniTreatmentDate`,
`infectionSite`,
`caseDefinition`,
`customId`,
`nationality`,
`outcomeDate`,
`registrationGroup`,
`registrationGroupOther`,
`diagnosisType`,
`PATIENT_ID`,
`NOTIFICATION_UNIT_ID`,
`drugResistanceType`,
`patientContactName`,
`WORKSPACE_ID`,
`OWNER_UNIT_ID`,
`TRANSFER_OUT_UNIT_ID`,
`REGIMEN_ID`,
`movedToIndividualized`,
`extrapulmonaryType`,
`extrapulmonaryType2`,
`pulmonaryType`,
`validated`,
`suspectClassification`,
`secDrugsReceived`,
`lastBmuDateTbRegister`,
`lastBmuTbRegistNumber`,
`movedSecondLineTreatment`
from tbcase;


insert into etbm3.casecomorbidities
(id,
case_id,
`alcoholExcessiveUse`,
`tobaccoUseWithin`,
`aids`,
`diabetes`,
`anaemia`,
`malnutrition`)
select
id,
id,
`alcoholExcessiveUse`,
`tobaccoUseWithin`,
`aids`,
`diabetes`,
`anaemia`,
`malnutrition`
from tbcase where alcoholExcessiveUse = true or tobaccoUseWithin=true
or aids=true or diabetes=true or anaemia=true or malnutrition=true;


INSERT INTO `etbm3`.`tags_case`
(`CASE_ID`,
`TAG_ID`)
select
`CASE_ID`,
`TAG_ID`
from tags_case;


INSERT INTO `etbm3`.`treatmenthealthunit`
(`id`,
`endDate`,
`iniDate`,
`CASE_ID`,
`UNIT_ID`,
`transferring`)
select
`id`,
`endDate`,
`iniDate`,
`CASE_ID`,
`UNIT_ID`,
`transferring`
from treatmenthealthunit;


INSERT INTO `etbm3`.`treatmentmonitoring`
(`id`,
`case_id`,
`month_treat`,
`year_treat`,
`day1`,
`day2`,
`day3`,
`day4`,
`day5`,
`day6`,
`day7`,
`day8`,
`day9`,
`day10`,
`day11`,
`day12`,
`day13`,
`day14`,
`day15`,
`day16`,
`day17`,
`day18`,
`day19`,
`day20`,
`day21`,
`day22`,
`day23`,
`day24`,
`day25`,
`day26`,
`day27`,
`day28`,
`day29`,
`day30`,
`day31`)
select
`id`,
`case_id`,
`month_treat`,
`year_treat`,
`day1`,
`day2`,
`day3`,
`day4`,
`day5`,
`day6`,
`day7`,
`day8`,
`day9`,
`day10`,
`day11`,
`day12`,
`day13`,
`day14`,
`day15`,
`day16`,
`day17`,
`day18`,
`day19`,
`day20`,
`day21`,
`day22`,
`day23`,
`day24`,
`day25`,
`day26`,
`day27`,
`day28`,
`day29`,
`day30`,
`day31`
from treatmentmonitoring;




INSERT INTO `etbm3`.`userlogin`
(`id`,
`Application`,
`IpAddress`,
`loginDate`,
`logoutDate`,
`lastAccess`,
`USER_ID`,
`WORKSPACE_ID`,
`sessionId`)
select
`id`,
`Application`,
`IpAddress`,
`loginDate`,
`logoutDate`,
`lastAccess`,
`USER_ID`,
`WORKSPACE_ID`,
`sessionId`
from userlogin;



INSERT INTO `etbm3`.`userpermission`
(`id`,
`canChange`,
`PROFILE_ID`,
`permission`)
select
`id`,
`canChange`,
`PROFILE_ID`,
`permission`
from userpermission;




INSERT INTO `etbm3`.`userworkspace_profiles`
(`userworkspace_id`,
`userprofile_id`)
select
`userworkspace_id`,
`userprofile_id`
from userworkspace_profiles;



INSERT INTO `etbm3`.`casecomment`
(`id`,
`comment`,
`comment_date`,
`CASE_ID`,
`USER_ID`,
`comment_group`)
select
`id`,
`comment`,
`comment_date`,
`CASE_ID`,
`USER_ID`,
`comment_group`
from casecomment;

INSERT INTO `etbm3`.`casecontact`
(`id`,
`age`,
`examinated`,
`gender`,
`name`,
`conduct`,
`contactType`,
`CASE_ID`,
`comments`,
`dateOfExamination`)
select
`id`,
`age`,
`examinated`,
`gender`,
`name`,
`conduct`,
`contactType`,
`CASE_ID`,
`comments`,
`dateOfExamination`
from casecontact;


INSERT INTO `etbm3`.`casesideeffect`
(`id`,
`medicines`,
`SE_MONTH`,
`resolved`,
`sideeffect`,
`CASE_ID`,
`SUBSTANCE_ID`,
`SUBSTANCE2_ID`,
`DTYPE`,
`actionTaken`,
`grade`,
`outcome`,
`seriousness`,
`DISCRIMINATOR`,
`effectEnd`,
`effectSt`,
`otherAdverseEffect`,
`comment`,
`dateChangeReg`,
`symptomTherapy`)
select
`id`,
`medicines`,
`SE_MONTH`,
`resolved`,
`sideeffect`,
`CASE_ID`,
`SUBSTANCE_ID`,
`SUBSTANCE2_ID`,
`DTYPE`,
`actionTaken`,
`grade`,
`outcome`,
`seriousness`,
`DISCRIMINATOR`,
`effectEnd`,
`effectSt`,
`otherAdverseEffect`,
`comment`,
`dateChangeReg`,
`symptomTherapy`
from casesideeffect;


INSERT INTO `etbm3`.`examdst`
(`id`,
`comments`,
`dateRelease`,
`LABORATORY_ID`,
`method`,
`CASE_ID`,
`sampleNumber`,
`EVENT_DATE`,
`sampleType`,
`request_id`,
`status`,
`resultAm`,
`resultCfz`,
`resultCm`,
`resultCs`,
`resultE`,
`resultEto`,
`resultH`,
`resultLfx`,
`resultOfx`,
`resultR`,
`resultS`,
`resultZ`)
select
`id`,
`comments`,
`dateRelease`,
`LABORATORY_ID`,
`method`,
`CASE_ID`,
`sampleNumber`,
`EVENT_DATE`,
`sampleType`,
`request_id`,
`status`,
`resultAm`,
`resultCfz`,
`resultCm`,
`resultCs`,
`resultE`,
`resultEto`,
`resultH`,
`resultLfx`,
`resultOfx`,
`resultR`,
`resultS`,
`resultZ`
from examdst;



INSERT INTO `etbm3`.`examhiv`
(`id`,
`comments`,
`EVENT_DATE`,
`laboratory`,
`result`,
`startedARTdate`,
`startedCPTdate`,
`CASE_ID`,
`resultDate`)
select
`id`,
`comments`,
`EVENT_DATE`,
`laboratory`,
`result`,
`startedARTdate`,
`startedCPTdate`,
`CASE_ID`,
`resultDate`
from examhiv;


INSERT INTO `etbm3`.`exammicroscopy`
(`id`,
`comments`,
`dateRelease`,
`result`,
`LABORATORY_ID`,
`numberOfAFB`,
`method`,
`sampleType`,
`CASE_ID`,
`sampleNumber`,
`EVENT_DATE`,
`request_id`,
`status`,
`otherSampleType`,
`visualAppearance`)
select
`id`,
`comments`,
`dateRelease`,
`result`,
`LABORATORY_ID`,
`numberOfAFB`,
`method`,
`sampleType`,
`CASE_ID`,
`sampleNumber`,
`EVENT_DATE`,
`request_id`,
`status`,
`otherSampleType`,
`visualAppearance`
from exammicroscopy;



INSERT INTO `etbm3`.`examxpert`
(`id`,
`case_ID`,
`EVENT_DATE`,
`comments`,
`dateRelease`,
`sampleNumber`,
`result`,
`laboratory_ID`,
`request_id`,
`status`)
select
`id`,
`case_ID`,
`EVENT_DATE`,
`comments`,
`dateRelease`,
`sampleNumber`,
`result`,
`laboratory_ID`,
`request_id`,
`status`
from examxpert;



INSERT INTO `etbm3`.`examxray`
(`id`,
`comments`,
`EVENT_DATE`,
`CASE_ID`,
`evolution`,
`presentation`)
select
`id`,
`comments`,
`EVENT_DATE`,
`CASE_ID`,
`evolution`,
`presentation`
from examxray;



INSERT INTO `etbm3`.`issue`
(`id`,
`case_id`,
`closed`,
`user_id`,
`creationDate`,
`title`,
`description`,
`unit_id`)
select
`id`,
`case_id`,
`closed`,
`user_id`,
`creationDate`,
`title`,
`description`,
`unit_id`
from issue;




INSERT INTO `etbm3`.`issuefollowup`
(`id`,
`issue_id`,
`text`,
`user_id`,
`followupDate`,
`unit_id`)
select
`id`,
`issue_id`,
`text`,
`user_id`,
`followupDate`,
`unit_id`
from issuefollowup;


INSERT INTO `etbm3`.`medicalexamination`
(`id`,
`comments`,
`EVENT_DATE`,
`appointmentType`,
`height`,
`reasonNotUsingPrescMedicines`,
`responsible`,
`positionResponsible`,
`usingPrescMedicines`,
`weight`,
`CASE_ID`)
select
`id`,
`comments`,
`EVENT_DATE`,
`appointmentType`,
`height`,
`reasonNotUsingPrescMedicines`,
`responsible`,
`positionResponsible`,
`usingPrescMedicines`,
`weight`,
`CASE_ID`
from medicalexamination;



INSERT INTO `etbm3`.`medicineregimen`
(`id`,
`defaultDoseUnit`,
`defaultFrequency`,
`iniDay`,
`days`,
`MEDICINE_ID`,
`REGIMEN_ID`)
select
`id`,
`defaultDoseUnit`,
`defaultFrequency`,
`iniDay`,
`days`,
`MEDICINE_ID`,
`REGIMEN_ID`
from medicineregimen;




INSERT INTO `etbm3`.`examculture`
(`id`,
`comments`,
`dateRelease`,
`result`,
`LABORATORY_ID`,
`method`,
`numberOfColonies`,
`sampleType`,
`CASE_ID`,
`sampleNumber`,
`EVENT_DATE`,
`request_id`,
`status`)
select
`id`,
`comments`,
`dateRelease`,
`result`,
`LABORATORY_ID`,
`method`,
`numberOfColonies`,
`sampleType`,
`CASE_ID`,
`sampleNumber`,
`EVENT_DATE`,
`request_id`,
`status`
from examculture;



INSERT INTO `etbm3`.`prescribedmedicine`
(`id`,
`doseUnit`,
`frequency`,
`PRODUCT_ID`,
`CASE_ID`,
`IniDate`,
`EndDate`,
`comments`)
select
`id`,
`doseUnit`,
`frequency`,
`PRODUCT_ID`,
`CASE_ID`,
`IniDate`,
`EndDate`,
`comments`
from prescribedmedicine;


INSERT INTO `etbm3`.`prevtbtreatment`
(`id`,
`TREATMENT_MONTH`,
`outcome`,
`TREATMENT_YEAR`,
`CASE_ID`,
`OUTCOME_MONTH`,
`OUTCOME_YEAR`,
`am`,
`cfz`,
`cm`,
`cs`,
`e`,
`eto`,
`h`,
`lfx`,
`ofx`,
`r`,
`s`,
`z`)
select
`id`,
`TREATMENT_MONTH`,
`outcome`,
`TREATMENT_YEAR`,
`CASE_ID`,
`OUTCOME_MONTH`,
`OUTCOME_YEAR`,
`am`,
`cfz`,
`cm`,
`cs`,
`e`,
`eto`,
`h`,
`lfx`,
`ofx`,
`r`,
`s`,
`z`
from prevtbtreatment;
