package com.raywu.investingsimulator.portfolio.asset;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
public class AssetPK implements Serializable {

    private int userId;
    private String symbol;

    public AssetPK() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetPK accountPK = (AssetPK) o;
        return userId == accountPK.userId &&
                symbol.equals(accountPK.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, symbol);
    }
}

/*

When the primary key of a table is a composited key, I have to create a separate composite
primary key class with these primary key columns

------ Note ------
The composite primary key class must be public, contains a no-argument constructor, defines
both equals() and hashCode() methods, and implements the Serializable interface.


*/