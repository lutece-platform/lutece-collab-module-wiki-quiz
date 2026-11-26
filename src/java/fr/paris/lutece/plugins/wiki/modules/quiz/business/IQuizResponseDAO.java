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
package fr.paris.lutece.plugins.wiki.modules.quiz.business;

import java.util.List;
import java.util.Optional;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Data access object interface for quiz responses.
 */
public interface IQuizResponseDAO
{
    /**
     * Inserts a new quiz response into the database.
     *
     * @param response
     *            The quiz response to insert
     * @param plugin
     *            The plugin instance
     */
    void insert( QuizResponse response, Plugin plugin );

    /**
     * Updates an existing quiz response in the database.
     *
     * @param response
     *            The quiz response to update
     * @param plugin
     *            The plugin instance
     */
    void store( QuizResponse response, Plugin plugin );

    /**
     * Retrieves all responses for a specific attempt.
     *
     * @param nIdAttempt
     *            The attempt ID
     * @param plugin
     *            The plugin instance
     * @return A list of quiz responses for the attempt
     */
    List<QuizResponse> selectByAttempt( int nIdAttempt, Plugin plugin );

    /**
     * Retrieves a response for a specific attempt and question combination.
     *
     * @param nIdAttempt
     *            The attempt ID
     * @param nIdQuestion
     *            The question ID
     * @param plugin
     *            The plugin instance
     * @return An optional containing the quiz response if found
     */
    Optional<QuizResponse> selectByAttemptAndQuestion( int nIdAttempt, int nIdQuestion, Plugin plugin );
}
