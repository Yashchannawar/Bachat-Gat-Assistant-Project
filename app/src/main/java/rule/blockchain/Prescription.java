package rule.blockchain;
public class Prescription {
    private String prescriptionId;
    private String patientName;
    private String patientAge;
    private String patientWeight;
    private String patientHeight;
    private String medicalCondition;
    private String hospitalName;
    private String description;
    private String date;
    private String time;
    private String prescriptionDownloadUrl;
    private String reportDownloadUrl;
    private String email;
    private String userId;

    // Default constructor (required by Firebase)
    public Prescription() {
    }

    // Constructor with parameters
    public Prescription(String prescriptionId, String patientName, String patientAge, String patientWeight, String patientHeight, String medicalCondition, String hospitalName, String description, String date, String time, String prescriptionDownloadUrl, String reportDownloadUrl, String email, String userId) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientWeight = patientWeight;
        this.patientHeight = patientHeight;
        this.medicalCondition = medicalCondition;
        this.hospitalName = hospitalName;
        this.description = description;
        this.date = date;
        this.time = time;
        this.prescriptionDownloadUrl = prescriptionDownloadUrl;
        this.reportDownloadUrl = reportDownloadUrl;
        this.email = email;
        this.userId = userId;
    }

    // Getters and setters (you can generate these automatically in Android Studio)
    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrescriptionDownloadUrl() {
        return prescriptionDownloadUrl;
    }

    public void setPrescriptionDownloadUrl(String prescriptionDownloadUrl) {
        this.prescriptionDownloadUrl = prescriptionDownloadUrl;
    }

    public String getReportDownloadUrl() {
        return reportDownloadUrl;
    }

    public void setReportDownloadUrl(String reportDownloadUrl) {
        this.reportDownloadUrl = reportDownloadUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId='" + prescriptionId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", patientWeight='" + patientWeight + '\'' +
                ", patientHeight='" + patientHeight + '\'' +
                ", medicalCondition='" + medicalCondition + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", prescriptionDownloadUrl='" + prescriptionDownloadUrl + '\'' +
                ", reportDownloadUrl='" + reportDownloadUrl + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
