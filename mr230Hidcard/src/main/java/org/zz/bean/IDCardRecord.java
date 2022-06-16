package org.zz.bean;

import android.graphics.Bitmap;



import java.util.Arrays;
import java.util.Date;


public class IDCardRecord {


    private Long id;
    /* 注释说明：二代证 / 港澳台 / 外国人永久居留证 */
    /** 卡片类型 空值=二代证，J=港澳台，I=外国人永久居留证 **/
    private String cardType;
    /** 物理编号 **/
    private String cardId;
    /** 姓名 **/
    private String name;
    /** 出生日期 **/
    private String birthday;
    /** 地址 / 地址 / 空值 **/
    private String address;
    /** 身份证号码 / 身份证号 / 永久居留证号码 **/
    private String cardNumber;
    /** 签发机构 / 签发机构 / 申请受理机关代码 **/
    private String issuingAuthority;
    /** 有效期开始 **/
    private String validateStart;
    /** 有效期结束 **/
    private String validateEnd;
    /** 男/女 **/
    private String sex;
    /** 民族 / 空值 / 国籍或所在地区代码 **/
    private String nation;

    /** 指纹0 **/
    private String fingerprint0;
    /** 指纹0指位 **/
    private String fingerprintPosition0;
    /** 指纹1 **/
    private String fingerprint1;
    /** 指纹1指位 **/
    private String fingerprintPosition1;

    /** 港澳台：通行证号码 **/
    private String passNumber;
    /** 港澳台：签发次数 **/
    private String issueCount;
    /** 外国人：中文姓名 **/
    private String chineseName;
    /** 外国人：证件版本号 **/
    private String version;

    /** 身份证照片路径 **/
    private String cardPhotoPath;
    /** 现场照片路径 **/
    private String facePhotoPath;

    /** 比对模式 **/
    private int verifyMode;
    /** 人脸比对结果 **/
    private boolean faceResult;
    /** 指纹比对结果 **/
    private boolean fingerResult;
    /** 人脸比对分数 **/
    private float faceScore;
    /** 指纹比对分数 **/
    private float fingerScore;
    /** 比对结果 **/
    private boolean verifyResult;
    /** 比对时间 **/
    private Date verifyTime;

    /** 机构名称（遗留） **/
    private String orgName;
    /** 结果描述（遗留） **/
    private String describe;
    /** 定位 **/
    private String location;
    /** 上传状态 **/
    private boolean upload;

    /** 身份证照片Bitmap **/

    private Bitmap cardBitmap;
    /** 现场照片Bitmap **/

    private Bitmap faceBitmap;
    /** 身份证照片人脸特征 **/

    private byte[] cardFeature;

    /** 通过指纹比对的指纹 **/

    private String localeFingerprint;
    /** 通过指纹比对的指纹的图像 **/

    private Bitmap localeFingerprintBitmap;
    /** 采集指纹1 **/

    private String gatherFingerprint1;
    /** 采集指纹1的图像 **/

    private Bitmap gatherFingerprintBitmap1;
    /** 采集指纹2 **/

    private String gatherFingerprint2;
    /** 采集指纹2的图像 **/

    private Bitmap gatherFingerprintBitmap2;
    public IDCardRecord(Long id, String cardType, String cardId, String name,
                        String birthday, String address, String cardNumber,
                        String issuingAuthority, String validateStart, String validateEnd,
                        String sex, String nation, String fingerprint0,
                        String fingerprintPosition0, String fingerprint1,
                        String fingerprintPosition1, String passNumber, String issueCount,
                        String chineseName, String version, String cardPhotoPath,
                        String facePhotoPath, int verifyMode, boolean faceResult,
                        boolean fingerResult, float faceScore, float fingerScore,
                        boolean verifyResult, Date verifyTime, String orgName, String describe,
                        String location, boolean upload) {
        this.id = id;
        this.cardType = cardType;
        this.cardId = cardId;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.cardNumber = cardNumber;
        this.issuingAuthority = issuingAuthority;
        this.validateStart = validateStart;
        this.validateEnd = validateEnd;
        this.sex = sex;
        this.nation = nation;
        this.fingerprint0 = fingerprint0;
        this.fingerprintPosition0 = fingerprintPosition0;
        this.fingerprint1 = fingerprint1;
        this.fingerprintPosition1 = fingerprintPosition1;
        this.passNumber = passNumber;
        this.issueCount = issueCount;
        this.chineseName = chineseName;
        this.version = version;
        this.cardPhotoPath = cardPhotoPath;
        this.facePhotoPath = facePhotoPath;
        this.verifyMode = verifyMode;
        this.faceResult = faceResult;
        this.fingerResult = fingerResult;
        this.faceScore = faceScore;
        this.fingerScore = fingerScore;
        this.verifyResult = verifyResult;
        this.verifyTime = verifyTime;
        this.orgName = orgName;
        this.describe = describe;
        this.location = location;
        this.upload = upload;
    }
//    @Generated(hash = 90006662)
    public IDCardRecord() {
    }

    private IDCardRecord(Builder builder) {
        setId(builder.id);
        setCardType(builder.cardType);
        setCardId(builder.cardId);
        setName(builder.name);
        setBirthday(builder.birthday);
        setAddress(builder.address);
        setCardNumber(builder.cardNumber);
        setIssuingAuthority(builder.issuingAuthority);
        setValidateStart(builder.validateStart);
        setValidateEnd(builder.validateEnd);
        setSex(builder.sex);
        setNation(builder.nation);
        setFingerprint0(builder.fingerprint0);
        setFingerprintPosition0(builder.fingerprintPosition0);
        setFingerprint1(builder.fingerprint1);
        setFingerprintPosition1(builder.fingerprintPosition1);
        setPassNumber(builder.passNumber);
        setIssueCount(builder.issueCount);
        setChineseName(builder.chineseName);
        setVersion(builder.version);
        setCardPhotoPath(builder.cardPhotoPath);
        setFacePhotoPath(builder.facePhotoPath);
        setVerifyMode(builder.verifyMode);
        setFaceResult(builder.faceResult);
        setFingerResult(builder.fingerResult);
        setFaceScore(builder.faceScore);
        setFingerScore(builder.fingerScore);
        setVerifyResult(builder.verifyResult);
        setVerifyTime(builder.verifyTime);
        setOrgName(builder.orgName);
        setDescribe(builder.describe);
        setLocation(builder.location);
        setUpload(builder.upload);
        setCardBitmap(builder.cardBitmap);
        setFaceBitmap(builder.faceBitmap);
        setCardFeature(builder.cardFeature);
        setLocaleFingerprint(builder.localeFingerprint);
        setLocaleFingerprintBitmap(builder.localeFingerprintBitmap);
        setGatherFingerprint1(builder.gatherFingerprint1);
        setGatherFingerprintBitmap1(builder.gatherFingerprintBitmap1);
        setGatherFingerprint2(builder.gatherFingerprint2);
        setGatherFingerprintBitmap2(builder.gatherFingerprintBitmap2);
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCardType() {
        return this.cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public String getCardId() {
        return this.cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCardNumber() {
        return this.cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getIssuingAuthority() {
        return this.issuingAuthority;
    }
    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }
    public String getValidateStart() {
        return this.validateStart;
    }
    public void setValidateStart(String validateStart) {
        this.validateStart = validateStart;
    }
    public String getValidateEnd() {
        return this.validateEnd;
    }
    public void setValidateEnd(String validateEnd) {
        this.validateEnd = validateEnd;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getNation() {
        return this.nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    public String getFingerprint0() {
        return this.fingerprint0;
    }
    public void setFingerprint0(String fingerprint0) {
        this.fingerprint0 = fingerprint0;
    }
    public String getFingerprintPosition0() {
        return this.fingerprintPosition0;
    }
    public void setFingerprintPosition0(String fingerprintPosition0) {
        this.fingerprintPosition0 = fingerprintPosition0;
    }
    public String getFingerprint1() {
        return this.fingerprint1;
    }
    public void setFingerprint1(String fingerprint1) {
        this.fingerprint1 = fingerprint1;
    }
    public String getFingerprintPosition1() {
        return this.fingerprintPosition1;
    }
    public void setFingerprintPosition1(String fingerprintPosition1) {
        this.fingerprintPosition1 = fingerprintPosition1;
    }
    public String getPassNumber() {
        return this.passNumber;
    }
    public void setPassNumber(String passNumber) {
        this.passNumber = passNumber;
    }
    public String getIssueCount() {
        return this.issueCount;
    }
    public void setIssueCount(String issueCount) {
        this.issueCount = issueCount;
    }
    public String getChineseName() {
        return this.chineseName;
    }
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getCardPhotoPath() {
        return this.cardPhotoPath;
    }
    public void setCardPhotoPath(String cardPhotoPath) {
        this.cardPhotoPath = cardPhotoPath;
    }
    public String getFacePhotoPath() {
        return this.facePhotoPath;
    }
    public void setFacePhotoPath(String facePhotoPath) {
        this.facePhotoPath = facePhotoPath;
    }
    public int getVerifyMode() {
        return this.verifyMode;
    }
    public void setVerifyMode(int verifyMode) {
        this.verifyMode = verifyMode;
    }
    public boolean getFaceResult() {
        return this.faceResult;
    }
    public void setFaceResult(boolean faceResult) {
        this.faceResult = faceResult;
    }
    public boolean getFingerResult() {
        return this.fingerResult;
    }
    public void setFingerResult(boolean fingerResult) {
        this.fingerResult = fingerResult;
    }
    public float getFaceScore() {
        return this.faceScore;
    }
    public void setFaceScore(float faceScore) {
        this.faceScore = faceScore;
    }
    public float getFingerScore() {
        return this.fingerScore;
    }
    public void setFingerScore(float fingerScore) {
        this.fingerScore = fingerScore;
    }
    public boolean getVerifyResult() {
        return this.verifyResult;
    }
    public void setVerifyResult(boolean verifyResult) {
        this.verifyResult = verifyResult;
    }
    public Date getVerifyTime() {
        return this.verifyTime;
    }
    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }
    public String getOrgName() {
        return this.orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    public String getDescribe() {
        return this.describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public boolean getUpload() {
        return this.upload;
    }
    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public boolean isFaceResult() {
        return faceResult;
    }

    public boolean isFingerResult() {
        return fingerResult;
    }

    public boolean isVerifyResult() {
        return verifyResult;
    }

    public boolean isUpload() {
        return upload;
    }

    public Bitmap getCardBitmap() {
        return cardBitmap;
    }

    public void setCardBitmap(Bitmap cardBitmap) {
        this.cardBitmap = cardBitmap;
    }

    public Bitmap getFaceBitmap() {
        return faceBitmap;
    }

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
    }

    public byte[] getCardFeature() {
        return cardFeature;
    }

    public void setCardFeature(byte[] cardFeature) {
        this.cardFeature = cardFeature;
    }

    public String getLocaleFingerprint() {
        return localeFingerprint;
    }

    public void setLocaleFingerprint(String localeFingerprint) {
        this.localeFingerprint = localeFingerprint;
    }

    public Bitmap getLocaleFingerprintBitmap() {
        return localeFingerprintBitmap;
    }

    public void setLocaleFingerprintBitmap(Bitmap localeFingerprintBitmap) {
        this.localeFingerprintBitmap = localeFingerprintBitmap;
    }

    public String getGatherFingerprint1() {
        return gatherFingerprint1;
    }

    public void setGatherFingerprint1(String gatherFingerprint1) {
        this.gatherFingerprint1 = gatherFingerprint1;
    }

    public Bitmap getGatherFingerprintBitmap1() {
        return gatherFingerprintBitmap1;
    }

    public void setGatherFingerprintBitmap1(Bitmap gatherFingerprintBitmap1) {
        this.gatherFingerprintBitmap1 = gatherFingerprintBitmap1;
    }

    public String getGatherFingerprint2() {
        return gatherFingerprint2;
    }

    public void setGatherFingerprint2(String gatherFingerprint2) {
        this.gatherFingerprint2 = gatherFingerprint2;
    }

    public Bitmap getGatherFingerprintBitmap2() {
        return gatherFingerprintBitmap2;
    }

    public void setGatherFingerprintBitmap2(Bitmap gatherFingerprintBitmap2) {
        this.gatherFingerprintBitmap2 = gatherFingerprintBitmap2;
    }

    public static final class Builder {
        private Long id;
        private String cardType;
        private String cardId;
        private String name;
        private String birthday;
        private String address;
        private String cardNumber;
        private String issuingAuthority;
        private String validateStart;
        private String validateEnd;
        private String sex;
        private String nation;
        private String fingerprint0;
        private String fingerprintPosition0;
        private String fingerprint1;
        private String fingerprintPosition1;
        private String passNumber;
        private String issueCount;
        private String chineseName;
        private String version;
        private String cardPhotoPath;
        private String facePhotoPath;
        private int verifyMode;
        private boolean faceResult;
        private boolean fingerResult;
        private float faceScore;
        private float fingerScore;
        private boolean verifyResult;
        private Date verifyTime;
        private String orgName;
        private String describe;
        private String location;
        private boolean upload;
        private Bitmap cardBitmap;
        private Bitmap faceBitmap;
        private byte[] cardFeature;
        private String localeFingerprint;
        private Bitmap localeFingerprintBitmap;
        private String gatherFingerprint1;
        private Bitmap gatherFingerprintBitmap1;
        private String gatherFingerprint2;
        private Bitmap gatherFingerprintBitmap2;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder cardType(String val) {
            cardType = val;
            return this;
        }

        public Builder cardId(String val) {
            cardId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder birthday(String val) {
            birthday = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder cardNumber(String val) {
            cardNumber = val;
            return this;
        }

        public Builder issuingAuthority(String val) {
            issuingAuthority = val;
            return this;
        }

        public Builder validateStart(String val) {
            validateStart = val;
            return this;
        }

        public Builder validateEnd(String val) {
            validateEnd = val;
            return this;
        }

        public Builder sex(String val) {
            sex = val;
            return this;
        }

        public Builder nation(String val) {
            nation = val;
            return this;
        }

        public Builder fingerprint0(String val) {
            fingerprint0 = val;
            return this;
        }

        public Builder fingerprintPosition0(String val) {
            fingerprintPosition0 = val;
            return this;
        }

        public Builder fingerprint1(String val) {
            fingerprint1 = val;
            return this;
        }

        public Builder fingerprintPosition1(String val) {
            fingerprintPosition1 = val;
            return this;
        }

        public Builder passNumber(String val) {
            passNumber = val;
            return this;
        }

        public Builder issueCount(String val) {
            issueCount = val;
            return this;
        }

        public Builder chineseName(String val) {
            chineseName = val;
            return this;
        }

        public Builder version(String val) {
            version = val;
            return this;
        }

        public Builder cardPhotoPath(String val) {
            cardPhotoPath = val;
            return this;
        }

        public Builder facePhotoPath(String val) {
            facePhotoPath = val;
            return this;
        }

        public Builder verifyMode(int val) {
            verifyMode = val;
            return this;
        }

        public Builder faceResult(boolean val) {
            faceResult = val;
            return this;
        }

        public Builder fingerResult(boolean val) {
            fingerResult = val;
            return this;
        }

        public Builder faceScore(float val) {
            faceScore = val;
            return this;
        }

        public Builder fingerScore(float val) {
            fingerScore = val;
            return this;
        }

        public Builder verifyResult(boolean val) {
            verifyResult = val;
            return this;
        }

        public Builder verifyTime(Date val) {
            verifyTime = val;
            return this;
        }

        public Builder orgName(String val) {
            orgName = val;
            return this;
        }

        public Builder describe(String val) {
            describe = val;
            return this;
        }

        public Builder location(String val) {
            location = val;
            return this;
        }

        public Builder upload(boolean val) {
            upload = val;
            return this;
        }

        public Builder cardBitmap(Bitmap val) {
            cardBitmap = val;
            return this;
        }

        public Builder faceBitmap(Bitmap val) {
            faceBitmap = val;
            return this;
        }

        public Builder cardFeature(byte[] val) {
            cardFeature = val;
            return this;
        }

        public Builder localeFingerprint(String val) {
            localeFingerprint = val;
            return this;
        }

        public Builder localeFingerprintBitmap(Bitmap val) {
            localeFingerprintBitmap = val;
            return this;
        }

        public Builder gatherFingerprint1(String val) {
            gatherFingerprint1 = val;
            return this;
        }

        public Builder gatherFingerprintBitmap1(Bitmap val) {
            gatherFingerprintBitmap1 = val;
            return this;
        }

        public Builder gatherFingerprint2(String val) {
            gatherFingerprint2 = val;
            return this;
        }

        public Builder gatherFingerprintBitmap2(Bitmap val) {
            gatherFingerprintBitmap2 = val;
            return this;
        }

        public IDCardRecord build() {
            return new IDCardRecord(this);
        }
    }

    @Override
    public String toString() {
        return "IDCardRecord{" +
                "id=" + id +
                ", cardType='" + cardType + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", validateStart='" + validateStart + '\'' +
                ", validateEnd='" + validateEnd + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", fingerprint0='" + fingerprint0 + '\'' +
                ", fingerprintPosition0='" + fingerprintPosition0 + '\'' +
                ", fingerprint1='" + fingerprint1 + '\'' +
                ", fingerprintPosition1='" + fingerprintPosition1 + '\'' +
                ", passNumber='" + passNumber + '\'' +
                ", issueCount='" + issueCount + '\'' +
                ", chineseName='" + chineseName + '\'' +
                ", version='" + version + '\'' +
                ", cardPhotoPath='" + cardPhotoPath + '\'' +
                ", facePhotoPath='" + facePhotoPath + '\'' +
                ", verifyMode=" + verifyMode +
                ", faceResult=" + faceResult +
                ", fingerResult=" + fingerResult +
                ", faceScore=" + faceScore +
                ", fingerScore=" + fingerScore +
                ", verifyResult=" + verifyResult +
                ", verifyTime=" + verifyTime +
                ", orgName='" + orgName + '\'' +
                ", describe='" + describe + '\'' +
                ", location='" + location + '\'' +
                ", upload=" + upload +
                ", cardBitmap=" + cardBitmap +
                ", faceBitmap=" + faceBitmap +
                ", cardFeature=" + Arrays.toString(cardFeature) +
                ", localeFingerprint='" + localeFingerprint + '\'' +
                ", localeFingerprintBitmap=" + localeFingerprintBitmap +
                ", gatherFingerprint1='" + gatherFingerprint1 + '\'' +
                ", gatherFingerprintBitmap1=" + gatherFingerprintBitmap1 +
                ", gatherFingerprint2='" + gatherFingerprint2 + '\'' +
                ", gatherFingerprintBitmap2=" + gatherFingerprintBitmap2 +
                '}';
    }
}
