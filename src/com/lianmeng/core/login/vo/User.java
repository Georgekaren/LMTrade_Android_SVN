package com.lianmeng.core.login.vo;



/**
 * 账户中心 UserInfo
 * @author liu
 *
 */
public class User {
	
	/**会员ID*/
	private int userId;
	
	/** 会员积分*/
	private int bonus;
	
	/** 会员等级 */
	private String level;
	
	/** session MD5*/
	private String usersession;
	
	/** 所下订单数*/
	private int ordercount;
	
	/** 收藏总数 */
	private int favoritescount;
	
	
	/**
	 * userName 用户名<br>
	 */
	private String userName;
	
	/**
     * telePhone 电话<br>
     */
    private String telephone;
	
    /**
     * pic 图像url<br>
     */
    private String pic;
    
	public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telePhone) {
        this.telephone = telePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User() {
	}

	public User(int userId, int bonus, String level, String usersession, int ordercount, int favoritescount,String userName,String telephone) {
		super();
		this.userId = userId;
		this.bonus = bonus;
		this.level = level;
		this.usersession = usersession;
		this.ordercount = ordercount;
		this.favoritescount = favoritescount;
		this.userName=userName;
		this.telephone=telephone;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUsersession() {
		return usersession;
	}

	public void setUsersession(String usersession) {
		this.usersession = usersession;
	}

	public int getOrdercount() {
		return ordercount;
	}

	public void setOrdercount(int ordercount) {
		this.ordercount = ordercount;
	}

	public int getFavoritescount() {
		return favoritescount;
	}

	public void setFavoritescount(int favoritescount) {
		this.favoritescount = favoritescount;
	}

	
}
