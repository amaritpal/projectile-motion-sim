package utils;

import javafx.scene.Scene;

/**
 * A singly linked list implementation for storing and manipulating {@link Scene} objects.
 */
public class LinkedList {

    private ListNode head;
    private ListNode tail;

    /**
     * Constructor for the LinkedList class. Initializes an empty list.
     */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Inserts a new scene at the end of the list.
     *
     * @param scene the scene to be inserted
     */
    public void insert(Scene scene) {

        ListNode newNode = new ListNode(scene);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    /**
     * Searches for a specific scene in the list.
     *
     * @param targetScene the scene to be searched for
     * @return true if the scene is found, false otherwise
     */
    public boolean search(Scene targetScene) {
        ListNode current = head;

        // Traverse the list and compare each scene with the targetScene
        while (current != null) {
            if (current.getScene().equals(targetScene)) {
                return true; // Scene found
            }
            current = current.next;
        }

        return false; // Scene not found
    }

    /**
     * Removes a specific scene from the list.
     *
     * @param targetScene the scene to be removed
     * @return true if the scene is removed successfully, false otherwise
     */
    public boolean remove(Scene targetScene) {
        if (isEmpty()) {
            System.out.println("List is empty. Cannot remove node.");
            return false;
        }

        // Special case: if the scene to remove is the head
        if (head.getScene().equals(targetScene)) {
            head = head.next;
            if (head == null) { // If the list becomes empty, update tail
                tail = null;
            }
            return true;
        }

        ListNode current = head;
        // Traverse the list to find the scene
        while (current != null && current.next != null) {
            if (current.next.getScene().equals(targetScene)) {
                current.next = current.next.next;
                // If we removed the last element, update the tail
                if (current.next == null) {
                    tail = current;
                }
                return true; // Scene removed
            }
            current = current.next;
        }

        // If the scene was not found
        System.out.println("Scene not found in the list.");
        return false;
    }

    /**
     * Retrieves the first scene in the list (the head scene).
     *
     * @return the head scene, or null if the list is empty
     */
    public Scene getHead() {
        if (!isEmpty()) {
            return head.getScene();
        }
        return null;
    }

    /**
     * Retrieves the last scene in the list (the tail scene).
     *
     * @return the tail scene, or null if the list is empty
     */
    public Scene getTail() {
        if (!isEmpty()) {
            return tail.getScene();
        }
        return null;
    }

    /**
     * Retrieves the head node of the list.
     *
     * @return the head node
     */
    public ListNode getHeadNode() {
        return head;
    }
}