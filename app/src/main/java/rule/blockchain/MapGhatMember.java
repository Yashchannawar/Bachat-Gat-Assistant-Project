package rule.blockchain;

public class MapGhatMember {
    private String GhatKey;
    private Member member;
    private String memberKey;
    private GhatGroup ghat;

    public String getGhatKey() {
        return GhatKey;
    }

    public void setGhatKey(String ghatKey) {
        GhatKey = ghatKey;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMemberKey() {
        return memberKey;
    }

    public void setMemberKey(String memberKey) {
        this.memberKey = memberKey;
    }

    public GhatGroup getGhat() {
        return ghat;
    }

    public void setGhat(GhatGroup ghat) {
        this.ghat = ghat;
    }
}
