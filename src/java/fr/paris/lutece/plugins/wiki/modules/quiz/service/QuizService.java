/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.modules.quiz.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.paris.lutece.plugins.wiki.business.item.AbstractWikiItem;
import fr.paris.lutece.plugins.wiki.business.item.WikiItemHome;
import fr.paris.lutece.plugins.wiki.business.item.WikiItemType;
import fr.paris.lutece.plugins.wiki.exception.WikiValidationException;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.Quiz;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswer;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswerHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAttempt;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAttemptHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestion;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestionHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizResponse;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizResponseHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;

/**
 * Service class for quiz management operations.
 */
public final class QuizService
{
    private static final String MESSAGE_VALIDATION_BOOK_NOT_FOUND = "module.wiki.quiz.validation.quiz.Book.notFound";
    private static final String MESSAGE_VALIDATION_BOOK_TYPE = "module.wiki.quiz.validation.quiz.Book.type";

    private static final int MIN_QUESTIONS_FOR_SHUFFLE = 2;
    private static final long MILLISECONDS_PER_SECOND = 1000L;
    private static final long SECONDS_PER_MINUTE = 60L;

    /**
     * Private constructor.
     */
    private QuizService( )
    {
    }

    /**
     * Finds a quiz by its ID.
     *
     * @param nQuizId
     *            the quiz ID
     * @return the quiz, or null if not found
     */
    public static Quiz findById( int nQuizId )
    {
        return QuizHome.findByPrimaryKey( nQuizId ).orElse( null );
    }

    /**
     * Retrieves all quizzes associated with a specific book.
     *
     * @param nIdBook
     *            the book ID
     * @return the list of quizzes
     */
    public static List<Quiz> getQuizzesByBook( int nIdBook )
    {
        return QuizHome.getQuizzesByBook( nIdBook );
    }

    /**
     * Retrieves all published quizzes associated with a specific book.
     *
     * @param nIdBook
     *            the book ID
     * @return the list of published quizzes
     */
    public static List<Quiz> getPublishedQuizzesByBook( int nIdBook )
    {
        return QuizHome.getPublishedQuizzesByBook( nIdBook );
    }

    /**
     * Creates a new quiz.
     *
     * @param quiz
     *            the quiz to create
     * @param locale
     *            the locale for error messages
     * @return the created quiz
     * @throws WikiValidationException
     *             if validation fails
     */
    public static Quiz create( Quiz quiz, Locale locale ) throws WikiValidationException
    {
        validateBook( quiz, locale );

        QuizHome.create( quiz );
        return quiz;
    }

    /**
     * Updates an existing quiz.
     *
     * @param quiz
     *            the quiz to update
     * @param locale
     *            the locale for error messages
     * @return the updated quiz
     * @throws WikiValidationException
     *             if validation fails
     */
    public static Quiz update( Quiz quiz, Locale locale ) throws WikiValidationException
    {
        validateBook( quiz, locale );

        QuizHome.update( quiz );
        return quiz;
    }

    /**
     * Deletes a quiz.
     *
     * @param nQuizId
     *            the quiz ID
     */
    public static void delete( int nQuizId )
    {
        QuizHome.remove( nQuizId );
    }

    /**
     * Retrieves a quiz along with its questions and answers.
     *
     * @param nQuizId
     *            the quiz ID
     * @return the quiz with questions and answers, or null if not found
     */
    public static Quiz getQuizWithQuestions( int nQuizId )
    {
        Quiz quiz = findById( nQuizId );
        if ( quiz != null )
        {
            List<QuizQuestion> questions = QuizQuestionHome.getQuestionsByQuiz( nQuizId );
            for ( QuizQuestion question : questions )
            {
                List<QuizAnswer> answers = QuizAnswerHome.getAnswersByQuestion( question.getId( ) );
                question.setAnswers( answers );
            }
            quiz.setQuestions( questions );
        }
        return quiz;
    }

    /**
     * Creates a new question.
     *
     * @param question
     *            the question to create
     * @return the created question
     */
    public static QuizQuestion createQuestion( QuizQuestion question )
    {
        if ( question.getDisplayOrder( ) == 0 )
        {
            question.setDisplayOrder( QuizQuestionHome.getNextDisplayOrder( question.getIdQuiz( ) ) );
        }
        QuizQuestionHome.create( question );
        return question;
    }

    /**
     * Updates an existing question.
     *
     * @param question
     *            the question to update
     * @return the updated question
     */
    public static QuizQuestion updateQuestion( QuizQuestion question )
    {
        QuizQuestionHome.update( question );
        return question;
    }

    /**
     * Deletes a question and its associated answers.
     *
     * @param nQuestionId
     *            the question ID
     */
    public static void deleteQuestion( int nQuestionId )
    {
        QuizAnswerHome.removeByQuestion( nQuestionId );
        QuizQuestionHome.remove( nQuestionId );
    }

    /**
     * Reorders questions based on the provided list of question IDs.
     *
     * @param questionIds
     *            the list of question IDs in the desired order
     */
    public static void reorderQuestions( List<Integer> questionIds )
    {
        int nOrder = 0;
        for ( Integer nQuestionId : questionIds )
        {
            Optional<QuizQuestion> optQuestion = QuizQuestionHome.findByPrimaryKey( nQuestionId );
            if ( optQuestion.isPresent( ) )
            {
                QuizQuestion question = optQuestion.get( );
                question.setDisplayOrder( nOrder++ );
                QuizQuestionHome.update( question );
            }
        }
    }

    /**
     * Replaces the answers for a question.
     *
     * @param nQuestionId
     *            the question ID
     * @param listAnswers
     *            the list of new answers
     */
    public static void replaceAnswers( int nQuestionId, List<QuizAnswer> listAnswers )
    {
        QuizAnswerHome.removeByQuestion( nQuestionId );
        int nOrder = 0;
        for ( QuizAnswer answer : listAnswers )
        {
            answer.setIdQuestion( nQuestionId );
            answer.setDisplayOrder( nOrder++ );
            QuizAnswerHome.create( answer );
        }
    }

    /**
     * Sets the source pages for a question.
     *
     * @param nQuestionId
     *            the question ID
     * @param listPageIds
     *            the list of page IDs
     */
    public static void setQuestionSourcePages( int nQuestionId, List<Integer> listPageIds )
    {
        QuizQuestionHome.setSourcePages( nQuestionId, listPageIds );
    }

    /**
     * Validates the book associated with a quiz.
     *
     * @param quiz
     *            the quiz to validate
     * @param locale
     *            the locale for error messages
     * @throws WikiValidationException
     *             if the book is invalid
     */
    private static void validateBook( Quiz quiz, Locale locale ) throws WikiValidationException
    {
        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) )
        {
            throw new WikiValidationException( I18nService.getLocalizedString( MESSAGE_VALIDATION_BOOK_NOT_FOUND, locale ) );
        }

        AbstractWikiItem book = optBook.get( );
        if ( book.getType( ) != WikiItemType.BOOK )
        {
            throw new WikiValidationException( I18nService.getLocalizedString( MESSAGE_VALIDATION_BOOK_TYPE, locale ) );
        }
    }

    // ========== Quiz Play Operations ==========

    /**
     * Checks if a user can start a new attempt for a quiz.
     *
     * @param quiz
     *            The quiz to check
     * @param strUserGuid
     *            The unique identifier of the user
     * @param bCanEdit
     *            Whether the user has edit permissions on the quiz
     * @return true if the user can start an attempt, false otherwise
     */
    public static boolean canStartAttempt( Quiz quiz, String strUserGuid, boolean bCanEdit )
    {
        if ( !quiz.isPublished( ) && !bCanEdit )
        {
            return false;
        }

        if ( quiz.hasMaxAttempts( ) )
        {
            int nAttemptCount = QuizAttemptHome.countAttemptsByQuizAndUser( quiz.getId( ), strUserGuid );
            return nAttemptCount < quiz.getMaxAttempts( );
        }

        return true;
    }

    /**
     * Starts a new quiz attempt for a user.
     *
     * @param nQuizId
     *            The identifier of the quiz
     * @param user
     *            The Lutece user starting the attempt
     * @return The created QuizAttempt object
     */
    public static QuizAttempt startAttempt( int nQuizId, LuteceUser user )
    {
        QuizAttempt attempt = new QuizAttempt( );
        attempt.setIdQuiz( nQuizId );
        attempt.setUserGuid( user.getName( ) );
        attempt.setStartedAt( new Timestamp( System.currentTimeMillis( ) ) );

        QuizAttemptHome.create( attempt );

        return attempt;
    }

    /**
     * Retrieves all questions for a quiz attempt with answers, applying shuffle if configured.
     *
     * @param quiz
     *            The quiz to get questions for
     * @return A list of QuizQuestion objects with their answers
     */
    public static List<QuizQuestion> getQuestionsForAttempt( Quiz quiz )
    {
        List<QuizQuestion> questions = QuizQuestionHome.getQuestionsByQuiz( quiz.getId( ) );

        for ( QuizQuestion question : questions )
        {
            List<QuizAnswer> answers = QuizAnswerHome.getAnswersByQuestion( question.getId( ) );

            if ( answers.size( ) > 1 )
            {
                List<QuizAnswer> shuffledAnswers = new ArrayList<>( answers );
                Collections.shuffle( shuffledAnswers );
                question.setAnswers( shuffledAnswers );
            }
            else
            {
                question.setAnswers( answers );
            }
        }

        if ( quiz.getRandomQuestions( ) && questions.size( ) >= MIN_QUESTIONS_FOR_SHUFFLE )
        {
            List<QuizQuestion> shuffled = new ArrayList<>( questions );
            Collections.shuffle( shuffled );
            return shuffled;
        }

        return questions;
    }

    /**
     * Submits an answer for a question in an attempt and calculates the score.
     *
     * @param nAttemptId
     *            The identifier of the attempt
     * @param nQuestionId
     *            The identifier of the question
     * @param strUserAnswer
     *            The user's answer
     * @return The QuizResponse object, or null if the question was not found
     */
    public static QuizResponse submitAnswer( int nAttemptId, int nQuestionId, String strUserAnswer )
    {
        Optional<QuizQuestion> optQuestion = QuizQuestionHome.findByPrimaryKey( nQuestionId );

        if ( optQuestion.isEmpty( ) )
        {
            return null;
        }

        QuizQuestion question = optQuestion.get( );
        QuizScoringService.ScoringResult result = QuizScoringService.scoreQuestion( question, strUserAnswer );

        Optional<QuizResponse> existingResponse = QuizResponseHome.findByAttemptAndQuestion( nAttemptId, nQuestionId );

        QuizResponse response;

        if ( existingResponse.isPresent( ) )
        {
            response = existingResponse.get( );
            response.setUserAnswer( strUserAnswer );
            response.setIsCorrect( result.isCorrect( ) );
            response.setPointsEarned( result.getPointsEarned( ) );
            QuizResponseHome.update( response );
        }
        else
        {
            response = new QuizResponse( );
            response.setIdAttempt( nAttemptId );
            response.setIdQuestion( nQuestionId );
            response.setUserAnswer( strUserAnswer );
            response.setIsCorrect( result.isCorrect( ) );
            response.setPointsEarned( result.getPointsEarned( ) );
            QuizResponseHome.create( response );
        }

        return response;
    }

    /**
     * Completes a quiz attempt by calculating the final score and determining pass/fail status.
     *
     * @param nAttemptId
     *            The identifier of the attempt to complete
     * @return The completed QuizAttempt object, or null if the attempt or quiz was not found
     */
    public static QuizAttempt completeAttempt( int nAttemptId )
    {
        Optional<QuizAttempt> optAttempt = QuizAttemptHome.findByPrimaryKey( nAttemptId );

        if ( optAttempt.isEmpty( ) )
        {
            return null;
        }

        QuizAttempt attempt = optAttempt.get( );
        Optional<Quiz> optQuiz = QuizHome.findByPrimaryKey( attempt.getIdQuiz( ) );

        if ( optQuiz.isEmpty( ) )
        {
            return null;
        }

        Quiz quiz = optQuiz.get( );
        List<QuizResponse> responses = QuizResponseHome.getResponsesByAttempt( nAttemptId );

        int nEarnedPoints = responses.stream( ).mapToInt( QuizResponse::getPointsEarned ).sum( );
        List<QuizQuestion> questions = QuizQuestionHome.getQuestionsByQuiz( quiz.getId( ) );
        int nTotalPoints = QuizScoringService.calculateTotalPoints( questions );
        int nPercentageScore = QuizScoringService.calculatePercentageScore( nEarnedPoints, nTotalPoints );
        boolean bPassed = nPercentageScore >= quiz.getPassingScore( );

        attempt.setScore( nPercentageScore );
        attempt.setPassed( bPassed );
        attempt.setCompletedAt( new Timestamp( System.currentTimeMillis( ) ) );

        QuizAttemptHome.update( attempt );

        return attempt;
    }

    /**
     * Retrieves a quiz attempt along with all its responses.
     *
     * @param nAttemptId
     *            The identifier of the attempt
     * @return The QuizAttempt object with responses populated, or null if not found
     */
    public static QuizAttempt getAttemptWithResponses( int nAttemptId )
    {
        Optional<QuizAttempt> optAttempt = QuizAttemptHome.findByPrimaryKey( nAttemptId );

        if ( optAttempt.isEmpty( ) )
        {
            return null;
        }

        QuizAttempt attempt = optAttempt.get( );
        List<QuizResponse> responses = QuizResponseHome.getResponsesByAttempt( nAttemptId );
        attempt.setResponses( responses );

        return attempt;
    }

    /**
     * Retrieves all attempts made by a user for a specific quiz.
     *
     * @param nQuizId
     *            The identifier of the quiz
     * @param strUserGuid
     *            The unique identifier of the user
     * @return A list of QuizAttempt objects
     */
    public static List<QuizAttempt> getUserAttempts( int nQuizId, String strUserGuid )
    {
        return QuizAttemptHome.getAttemptsByQuizAndUser( nQuizId, strUserGuid );
    }

    /**
     * Retrieves an in-progress (incomplete) attempt for a user on a specific quiz.
     *
     * @param nQuizId
     *            The identifier of the quiz
     * @param strUserGuid
     *            The unique identifier of the user
     * @return The in-progress QuizAttempt, or null if none exists
     */
    public static QuizAttempt getInProgressAttempt( int nQuizId, String strUserGuid )
    {
        List<QuizAttempt> attempts = getUserAttempts( nQuizId, strUserGuid );
        return attempts.stream( ).filter( a -> !a.isCompleted( ) ).findFirst( ).orElse( null );
    }

    /**
     * Calculates the remaining time in seconds for a timed quiz attempt.
     *
     * @param attempt
     *            The quiz attempt
     * @param quiz
     *            The quiz with time limit configuration
     * @return The remaining time in seconds, or -1 if no time limit is set or attempt is completed
     */
    public static long calculateTimeRemainingSeconds( QuizAttempt attempt, Quiz quiz )
    {
        if ( quiz.getTimeLimitMinutes( ) == null || attempt.isCompleted( ) )
        {
            return -1;
        }

        long startedAtMs = attempt.getStartedAt( ).getTime( );
        long timeLimitMs = quiz.getTimeLimitMinutes( ) * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
        long elapsedMs = System.currentTimeMillis( ) - startedAtMs;
        long remainingMs = timeLimitMs - elapsedMs;

        return Math.max( 0, remainingMs / MILLISECONDS_PER_SECOND );
    }

    /**
     * Checks if the time has expired for a timed quiz attempt.
     *
     * @param attempt
     *            The quiz attempt
     * @param quiz
     *            The quiz with time limit configuration
     * @return true if time has expired, false otherwise
     */
    public static boolean isTimeExpired( QuizAttempt attempt, Quiz quiz )
    {
        if ( quiz.getTimeLimitMinutes( ) == null )
        {
            return false;
        }

        return calculateTimeRemainingSeconds( attempt, quiz ) == 0;
    }
}
