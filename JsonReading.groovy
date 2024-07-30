import groovy.json.JsonSlurper

// Load the JSON data from a file
def jsonFile = new File('tools.json')
def jsonContent = jsonFile.text
def jsonData = new JsonSlurper().parseText(jsonContent)

// Function to find the location based on tool type, name, OS, and version
def findLocation(toolType, toolName, os, version) {
    // Find the tool type category
    def toolCategory = jsonData["${toolType}Tools"]
    if (!toolCategory) return "Tool type not found"

    // Find the tool within the category
    def tool = toolCategory[toolName]
    if (!tool) return "Tool not found"

    // Find the OS
    def osData = tool[os]
    if (!osData) return "OS not found"

    // Find the version
    return osData[version] ?: "Version not found"
}

// Example usage
println findLocation("build", "Maven", "Linux", "1.0.0")  // Output: Linux Location
println findLocation("runtime", "DotNetCore", "Windows", "2.0.0")  // Output: Windows dir path


==================================================
import groovy.json.JsonSlurper

// Load and parse JSON data from file
def jsonData = new JsonSlurper().parseText(new File('tools.json').text)

// Function to find the location based on tool type, name, OS, and version
def findLocation(toolType, toolName, os, version) {
    jsonData["${toolType}Tools"]?.get(toolName)?.get(os)?.get(version) ?: "Not found"
}

// Example usage
println findLocation("build", "Maven", "Linux", "1.0.0")  // Output: Linux Location
println findLocation("runtime", "DotNetCore", "Windows", "2.0.0")  // Output: Windows dir path
==================================================

pipeline {
    agent any

    stages {
        stage('Read JSON') {
            steps {
                script {
                    // Read JSON file
                    def jsonData = readJSON file: 'tools.json'
                    
                    // Function to find the location based on tool type, name, OS, and version
                    def findLocation = { toolType, toolName, os, version ->
                        return jsonData["${toolType}Tools"]?.get(toolName)?.get(os)?.get(version) ?: "Not found"
                    }
                    
                    // Example usage
                    echo findLocation("build", "Maven", "Linux", "1.0.0")  // Output: Linux Location
                    echo findLocation("runtime", "DotNetCore", "Windows", "2.0.0")  // Output: Windows dir path
                }
            }
        }
    }
}


