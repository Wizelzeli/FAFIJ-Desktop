package sample.data.otherdata;


import sample.data.postbodies.JournalName;

public class Invitation {

    private JournalName journalName;
    private Role role;

    public JournalName getJournalName() {
        return journalName;
    }

    public void setJournalName(JournalName journalName) {
        this.journalName = journalName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}


