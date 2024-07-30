import groovy.json.JsonSlurper

pipeline {
    agent any

    stages {
        stage('Generate HTML Table') {
            steps {
                script {
                    // Example JSON string
                    def jsonString = '''
                    {
                        "buildTools": {
                            "Maven": {
                                "Linux": {
                                    "1.0.0": "Linux Location",
                                    "2.0.0": "Linux Location"
                                },
                                "Windows": {
                                    "1.0.0": "Windows dir path",
                                    "2.0.0": "Windows dir path"
                                }
                            },
                            "Gradle": {
                                "Linux": {
                                    "1.0.0": "New Linux Location"
                                },
                                "Windows": {
                                    "1.0.0": "New Windows dir path"
                                }
                            }
                        },
                        "runtimeTools": {
                            "DotNetCore": {
                                "Linux": {
                                    "1.0.0": "Linux Location",
                                    "2.0.0": "Linux Location"
                                },
                                "Windows": {
                                    "1.0.0": "Windows dir path",
                                    "2.0.0": "Windows dir path"
                                }
                            }
                        }
                    }
                    '''

                    // Parse JSON string using JsonSlurper
                    def jsonData = new JsonSlurper().parseText(jsonString)

                    // Function to generate HTML table from JSON data
                    def generateHtmlTable = { jsonData ->
                        def html = new StringBuilder()
                        html.append('<table border="1">')
                        html.append('<thead><tr><th>Tool Type</th><th>Tool Name</th><th>OS</th><th>Version</th><th>Location</th></tr></thead>')
                        html.append('<tbody>')

                        jsonData.each { toolType, tools ->
                            tools.each { toolName, osVersions ->
                                osVersions.each { os, versions ->
                                    versions.each { version, location ->
                                        html.append("<tr>")
                                        html.append("<td>${toolType}</td>")
                                        html.append("<td>${toolName}</td>")
                                        html.append("<td>${os}</td>")
                                        html.append("<td>${version}</td>")
                                        html.append("<td>${location}</td>")
                                        html.append("</tr>")
                                    }
                                }
                            }
                        }

                        html.append('</tbody>')
                        html.append('</table>')
                        return html.toString()
                    }

                    // Generate HTML table
                    def htmlTable = generateHtmlTable(jsonData)

                    // Output the HTML table
                    echo """<html>
                            <body>
                            <h2>Tool Information</h2>
                            ${htmlTable}
                            </body>
                            </html>"""
                }
            }
        }
    }
}
