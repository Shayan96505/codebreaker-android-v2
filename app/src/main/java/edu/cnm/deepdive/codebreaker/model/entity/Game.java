package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.UUID;

// creating a game entity class for our codebreaker app.
@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(
    indices = {
        @Index(value = {"game_key"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey(
            entity = Match.class,
            parentColumns = {"match_id"}, childColumns = {"match_id"},
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Game {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_id")
  private long id;

  @ColumnInfo(name =  "game_key", typeAffinity = ColumnInfo.BLOB)
  private UUID gameKey;

  // This is an object type long, because the value needs to be nullable, because the user may
  //want to play local games, not associated with an online match
  @ColumnInfo( name = "match_id", index = true)
  private Long matchId;

  @NonNull
  private String pool;

  private String code;

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @NonNull
  @ColumnInfo(index = true)
  private Date started = new Date();

  // setters and getters down below


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UUID getGameKey() {
    return gameKey;
  }

  public void setGameKey(UUID gameKey) {
    this.gameKey = gameKey;
  }

  public Long getMatchId() {
    return matchId;
  }

  public void setMatchId(Long matchId) {
    this.matchId = matchId;
  }

  @NonNull
  public String getPool() {
    return pool;
  }

  public void setPool(@NonNull String pool) {
    this.pool = pool;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  @NonNull
  public Date getStarted() {
    return started;
  }

  public void setStarted(@NonNull Date started) {
    this.started = started;
  }
}
