package edu.ecnu.journalmanage.model;

public enum ReviewType {
    preliminaryReview, preliminaryRebuttalReview,
    externalReview, externalRebuttalReview,
    finalReview, finalRebuttalReview;

    public String getString() {
        switch (this) {
            case preliminaryReview:
                return "preliminaryReview";
            case preliminaryRebuttalReview:
                return "preliminaryRebuttalReview";
            case externalReview:
                return "externalReview";
            case externalRebuttalReview:
                return "externalRebuttalReview";
            case finalReview:
                return "finalReview";
            case finalRebuttalReview:
                return "finalRebuttalReview";
            default:
                return "";
        }
    }
}
