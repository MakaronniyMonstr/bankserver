DO
$$
    DECLARE
        i int DEFAULT 0;

    BEGIN
        WHILE i < 5000
            LOOP
                INSERT INTO balance VALUES (i, 0, 0);
                i = i + 1;
            END LOOP;
    END
$$;