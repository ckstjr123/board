package hello.qnaboard.formatter;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.exception.NotFoundBoardTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * {boardtype} 경로 변수를 BoardType으로 parse,
 * BoardType을 렌더링되는 게시판 타이틀 명으로 print
 */
@Slf4j
public class BoardTypeFormatter implements Formatter<BoardType> {

    @Override
    public BoardType parse(String boardTypePathVar, Locale locale) throws ParseException {
        try {
            // 관례적으로 url에는 소문자, enum 타입은 대문자를 사용하기 때문에 upperCase로 바꿔서 분석
            return BoardType.valueOf(boardTypePathVar.toUpperCase(locale)); // ex) free → FREE
        } catch (IllegalArgumentException ex) {
            // @ResponseStatus(HttpStatus.NOT_FOUND) 404 응답
            throw new NotFoundBoardTypeException(ex.getMessage());
        }
    }

    @Override
    public String print(BoardType boardType, Locale locale) {
        return boardType.getTitle();
    }
}
