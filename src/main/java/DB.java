import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.Transaction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;

// Создание таблицы :

        // CREATE TABLE demoJson(
        // id SERIAL NOT NULL PRIMARY KEY,
        // jsonData JSONB NOT NULL
        //  );

@Entity
@Table(name = "demoJson")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
        ,
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class DB implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int dataId;

    @Type(type = "jsonb")
    @Column(name = "jsonData", columnDefinition = "jsonb")
    private String jsonData;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public static void main(String[] arg, SessionBuilder sf) {
        try {
            DB obj = new DB();
            JSONObject jsonObj = new JSONObject();
            Map m1 = new LinkedHashMap(2);
            m1.put("oldValue", "Active");
            m1.put("newValue", "InActive");
            jsonObj.put("status", m1);
            Map m2 = new LinkedHashMap(2);
            m2.put("oldValue", "Test 6");
            m2.put("newValue", "Test 6 updated");
            jsonObj.put("taskDetails", m2);
            obj.setJsonData(jsonObj.toString());
            Session session = null;
            Transaction tx = null;
            try {
                session = sf.openSession();
                tx = session.beginTransaction();
                session.save(obj);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

}