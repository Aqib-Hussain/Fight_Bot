package Main;



public class Fight {
    private String[] userArray;
    private String user1;
    private String user2;


    public void initiateFight(String[] arrayOfServerMessages){
        userArray = arrayOfServerMessages[1].split("!");
        user1 = userArray[0];
        user2 = arrayOfServerMessages[2];
        System.out.println(user1);
        System.out.println(user2);
    }

    private void damageCalculator(){

    }



}
