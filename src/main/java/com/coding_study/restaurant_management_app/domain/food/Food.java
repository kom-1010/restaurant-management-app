package com.coding_study.restaurant_management_app.domain.food;

import com.coding_study.restaurant_management_app.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn()
public abstract class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    @ManyToMany(mappedBy = "foods")
    private List<Category> categories = new ArrayList<>();

    public Food(String name, int price, Category category){
        this.name = name;
        this.price = price;
        this.categories.add(category);
    }

    protected void update(String name, int price){
        this.name = name;
        this.price = price;
    }

    public void addCategory(Category category){
        this.categories.add(category);
    }

    abstract public void update(String name, int price, int size);
}
