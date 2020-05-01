package Main;


import java.util.Random;

public class Fight {
    private String[] userArray;
    private String user1;
    private String user2;
    private int user1HP = 100;
    private int user2HP = 100;
    private int low;
    private int high;
    private int damageResultUser1;
    private int damageResultUser2;
    private int attackType;
    private String attack;
    private String winner;

    public Fight() {
    }

    // Initiate the fight
    public void initiateFight(String[] arrayOfServerMessages, String[] prefixArray) {
        // Assign user 1 to attacker, user 2 to attacked
        userArray = prefixArray[1].split("!");
        user1 = userArray[0];
        if (arrayOfServerMessages.length > 2) {
            user2 = arrayOfServerMessages[2];
        } else {
            user2 = user1 + "'s will to live";
        }
        user1HP = 100;
        user2HP = 100;
    }

    private void calculateAttackType() {
        if (attackType == 1) {
            attack = "tickles ";
        }
        if (attackType == 2) {
            attack = "pokes ";
        }
        if (attackType == 3) {
            attack = "punches ";
        }
        if (attackType == 4) {
            attack = "kicks ";
        }
        if (attackType == 5) {
            attack = "applies a Sharpshooter on ";
        }
        if (attackType == 6) {
            attack = "pulls out MR SOCKO on ";
        }
        if (attackType == 7) {
            attack = "Elbow drops ";
        }
        if (attackType == 8) {
            attack = "Stone Cold Stunners ";
        }
        if (attackType == 9) {
            attack = "pulls guard on ";
        }
    }

    public void calculateDamageUser1() {
        Random r = new Random();
        low = 1;
        high = 30;
        damageResultUser1 = r.nextInt(high - low) + low;

        while (user1HP > 0 && user2HP > 0) {
            if (damageResultUser1 < 6) {
                attackType = 1;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 11) {
                attackType = 2;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 16) {
                attackType = 3;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 18) {
                attackType = 4;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 21) {
                attackType = 5;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 24) {
                attackType = 6;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 27) {
                attackType = 7;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 30) {
                attackType = 8;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            } else if (damageResultUser1 < 31) {
                attackType = 9;
                calculateAttackType();
                user2HP = user2HP - damageResultUser1;
                break;
            }
        }
        winnerFound();
    }

    public void calculateDamageUser2() {
        Random s = new Random();
        low = 1;
        high = 30;
        damageResultUser2 = s.nextInt(high - low) + low;

        while (user1HP > 0 && user2HP > 0) {
            if (damageResultUser2 < 6) {
                attackType = 1;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 11) {
                attackType = 2;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 16) {
                attackType = 3;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 18) {
                attackType = 4;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 21) {
                attackType = 5;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 24) {
                attackType = 6;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 27) {
                attackType = 7;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 30) {
                attackType = 8;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            } else if (damageResultUser2 < 31) {
                attackType = 9;
                calculateAttackType();
                user1HP = user1HP - damageResultUser2;
                break;
            }
        }
        winnerFound();

    }

    public void winnerFound(){
        if (user1HP <= 0){
            winner = user2;
        } else {
            winner = user1;
        }
    }

    public int getUser1HP() {
        return user1HP;
    }

    public void setUser1HP(int user1HP) {
        this.user1HP = user1HP;
    }

    public int getUser2HP() {
        return user2HP;
    }

    public void setUser2HP(int user2HP) {
        this.user2HP = user2HP;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public String getAttack() {
        return attack;
    }

    public int getDamageResultUser1() {
        return damageResultUser1;
    }

    public int getDamageResultUser2() {
        return damageResultUser2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
