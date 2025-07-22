package nro.art;
import java.util.*;

public class DataRewardBuilder {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter the number of items:");
        int itemCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        
        String dataReward = "";
        boolean validData = false;
        
        while (!validData) {
            StringBuilder dataRewardBuilder = new StringBuilder();
            
            for (int i = 0; i < itemCount; i++) {
                System.out.println("Enter information for item " + (i + 1) + " (itemId:quantity|option1:option2,...):");
                String itemInfo = scanner.nextLine();
                if (checkSyntax(itemInfo)) { // Check syntax before adding to dataRewardBuilder
                    dataRewardBuilder.append(itemInfo);
                    if (i < itemCount - 1) {
                        dataRewardBuilder.append(";");
                    }
                } else {
                    System.out.println("Invalid input format for item " + (i + 1) + ". Please try again.");
                    i--; // Re-enter information for the current item
                }
            }
            
            dataReward = "{" + dataRewardBuilder.toString() + "}"; // Wrap in {}
            validData = validateData(dataReward);
            
            if (!validData) {
                System.out.println("Invalid input format for data reward. Please try again.");
            }
        }
        
        System.out.println("Created reward data string: " + dataReward);
        
        // Check syntax
        if (checkSyntax(dataReward)) {
            System.out.println("Syntax is correct.");
            parseDataReward(dataReward);
        } else {
            System.out.println("Syntax is incorrect.");
        }
        
        scanner.close();
    }
    
    public static boolean validateData(String data) {
        // Add your validation logic here
        // For simplicity, let's assume any non-empty string is valid
        return !data.isEmpty();
    }
    
    public static boolean checkSyntax(String itemInfo) {
        try {
            String[] subItemInfo = itemInfo.replaceAll("[{}\\[\\]]", "").split("\\|");
            String[] baseInfo = subItemInfo[0].split(":");
            int itemId = Integer.parseInt(baseInfo[0]);
            int quantity = Integer.parseInt(baseInfo[1]);
            if (subItemInfo.length == 2) {
                String[] options = subItemInfo[1].split(",");
                for (String opt : options) {
                    if (opt == null || opt.equals("")) {
                        continue;
                    }
                    String[] optInfo = opt.trim().split(":");
                    if (optInfo.length != 2) {
                        throw new NumberFormatException();
                    }
                    int tempIdOption = Integer.parseInt(optInfo[0]);
                    int param = Integer.parseInt(optInfo[1]);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void parseDataReward(String dataReward) {
        ArrayList<Item> itemsReward = new ArrayList<>();
        String[] itemsRewardArray = dataReward.substring(1, dataReward.length() - 1).split(";");
        
        for (String itemInfo : itemsRewardArray) {
            if (itemInfo == null || itemInfo.equals("")) {
                continue;
            }
            
            String[] subItemInfo = itemInfo.replaceAll("[{}\\[\\]]", "").split("\\|");
            String[] baseInfo = subItemInfo[0].split(":");
            int itemId = Integer.parseInt(baseInfo[0]);
            int quantity = Integer.parseInt(baseInfo[1]);
            Item item = new Item(itemId, quantity);
            
            if (subItemInfo.length == 2) {
                String[] options = subItemInfo[1].split(",");
                for (String opt : options) {
                    if (opt == null || opt.equals("")) {
                        continue;
                    }
                    String[] optInfo = opt.split(":");
                    int tempIdOption = Integer.parseInt(optInfo[0]);
                    int param = Integer.parseInt(optInfo[1]);
                    item.itemOptions.add(new ItemOption(tempIdOption, param));
                }
            }
            itemsReward.add(item);
        }
        
        // Print the result
        System.out.println("List of reward items:");
        for (Item item : itemsReward) {
            System.out.println(item);
        }
    }
}

class Item {
    int itemId;
    int quantity;
    ArrayList<ItemOption> itemOptions;
    
    public Item(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.itemOptions = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Item ID: ").append(itemId).append(", Quantity: ").append(quantity);
        if (!itemOptions.isEmpty()) {
            sb.append(", Options: ");
            for (ItemOption option : itemOptions) {
                sb.append("{").append(option.tempIdOption).append(":").append(option.param).append("} ");
            }
        }
        return sb.toString();
    }
}

class ItemOption {
    int tempIdOption;
    int param;
    
    public ItemOption(int tempIdOption, int param) {
        this.tempIdOption = tempIdOption;
        this.param = param;
    }
}
