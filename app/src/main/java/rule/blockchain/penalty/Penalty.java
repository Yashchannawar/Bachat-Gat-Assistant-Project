package rule.blockchain.penalty;

import rule.blockchain.Member;

public class Penalty {
    private String memberName;
    private String penaltyAmount;
     private String penaltyDescription;
    private Member member;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getPenaltyDescription() {
        return penaltyDescription;
    }

    public void setPenaltyDescription(String penaltyDescription) {
        this.penaltyDescription = penaltyDescription;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
