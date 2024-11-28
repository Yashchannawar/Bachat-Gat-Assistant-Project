package rule.blockchain;

public class GhatGroup {

    private String groupName;
    private String groupKey;
    private int totalAmount;
    private int monthlyAmount;

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    private int totalMembers;
    private String nameOfPresident;
    private String mobileOfPresident;
    private String chairmanEmail;
    private String groupPassword;

    // Constructor
    public GhatGroup(String groupName, int totalAmount, int monthlyAmount, int totalMembers,
                     String nameOfPresident, String mobileOfPresident, String chairmanEmail,
                     String groupPassword) {
        this.groupName = groupName;
        this.totalAmount = totalAmount;
        this.monthlyAmount = monthlyAmount;
        this.totalMembers = totalMembers;
        this.nameOfPresident = nameOfPresident;
        this.mobileOfPresident = mobileOfPresident;
        this.chairmanEmail = chairmanEmail;
        this.groupPassword = groupPassword;
    }

    public GhatGroup(){
    }

    // Getters and setters
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(int monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public String getNameOfPresident() {
        return nameOfPresident;
    }

    public void setNameOfPresident(String nameOfPresident) {
        this.nameOfPresident = nameOfPresident;
    }

    public String getMobileOfPresident() {
        return mobileOfPresident;
    }

    public void setMobileOfPresident(String mobileOfPresident) {
        this.mobileOfPresident = mobileOfPresident;
    }

    public String getChairmanEmail() {
        return chairmanEmail;
    }

    public void setChairmanEmail(String chairmanEmail) {
        this.chairmanEmail = chairmanEmail;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    @Override
    public String toString() {
        return "GhatGroup{" +
                "groupName='" + groupName + '\'' +
                ", groupKey='" + groupKey + '\'' +
                ", totalAmount=" + totalAmount +
                ", monthlyAmount=" + monthlyAmount +
                ", totalMembers=" + totalMembers +
                ", nameOfPresident='" + nameOfPresident + '\'' +
                ", mobileOfPresident='" + mobileOfPresident + '\'' +
                ", chairmanEmail='" + chairmanEmail + '\'' +
                ", groupPassword='" + groupPassword + '\'' +
                '}';
    }
}
