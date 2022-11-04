package com.raywu.investingsimulator.portfolio.asset;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
public class AssetPK implements Serializable {

    private int user_id;
    private String symbol;

    public AssetPK() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetPK accountPK = (AssetPK) o;
        return user_id == accountPK.user_id &&
                symbol.equals(accountPK.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, symbol);
    }
}

/*

When the primary key of a table is a composited primary key, I have to create a separate composite
primary key class with both these primary key columns

------ Note ------
The composite primary key class must be public, contains a no-argument constructor, defines
both equals() and hashCode() methods, and implements the Serializable interface.


*/