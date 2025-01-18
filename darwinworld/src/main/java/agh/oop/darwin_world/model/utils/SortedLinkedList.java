package agh.oop.darwin_world.model.utils;

public class SortedLinkedList<T extends Comparable<T>> {
    Node<T> head;


    public void add(T newObject) {
        Node<T> newNode = new Node<>(newObject);

        // Jeśli lista jest pusta lub nowy element jest mniejszy/równy niż głowa
        if (head == null || head.object.compareTo(newObject) <= 0) {
            newNode.next = head;
            head = newNode;
            return;
        }

        // Znajdowanie odpowiedniego miejsca dla nowego elementu
        Node<T> current = head;
        while (current.next != null && current.next.object.compareTo(newObject) <= 0) {
            current = current.next;
        }

        // Wstawienie nowego elementu
        newNode.next = current.next;
        current.next = newNode;
    }


    public boolean isEmpty() {
        return head == null;
    }

    public boolean remove(T value) {
        if (head == null) {
            return false; // Lista jest pusta
        }

        // Sprawdzenie, czy głowa jest elementem do usunięcia
        if (head.object.equals(value)) {
            head = head.next; // Przesunięcie głowy na następny węzeł
            return true;
        }

        // Szukanie elementu do usunięcia
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.object.equals(value)) {
                current.next = current.next.next; // Pominięcie węzła z wartością
                return true;
            }
            current = current.next;
        }

        return false; // Element nie został znaleziony
    }

    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.object + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    public T getHead(){
        return head.object;
    }


}
