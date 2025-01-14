package com.wmy.view;

public class AstNode {
    public String token;
    public AstNode left;
    public AstNode right;

    public AstNode(String token) {
        this.token = token;
    }

    public AstNode(String token, AstNode left) {
        this.token = token;
        this.left = left;
    }

    public AstNode(String token, AstNode left, AstNode right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

    public void add(AstNode node) {
        if (left == null) {
            left = node;
        } else if (right == null) {
            right = node;
        } else {
            right.add(node);
        }
    }
}