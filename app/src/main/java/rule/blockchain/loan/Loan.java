package rule.blockchain.loan;

import rule.blockchain.Member;

public class Loan {
    private String memberName;
    private String loanAmount;
    private String loanIntrest;
    private String loanTenure;
    private Member member;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanIntrest() {
        return loanIntrest;
    }

    public void setLoanIntrest(String loanIntrest) {
        this.loanIntrest = loanIntrest;
    }

    public String getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(String loanTenure) {
        this.loanTenure = loanTenure;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
