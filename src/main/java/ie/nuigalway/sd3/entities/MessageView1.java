/**
 * @file MessageView1.java
 *
 * A standard message entity with some extra fields from user table
 */

package ie.nuigalway.sd3.entities;

import java.util.Date;

public class MessageView1 extends Message {

    private String name;
    private boolean is_support;

    public MessageView1() {
    }

    public MessageView1(Long user_id, Long thread_id, String comment, Date dt_created, String name, boolean is_support) {
        super(user_id, thread_id, comment, dt_created);
        this.name = name;
        this.is_support = is_support;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSupport() {
        return is_support;
    }

    public void setIsSupport(boolean is_support) {
        this.is_support = is_support;
    }
}
